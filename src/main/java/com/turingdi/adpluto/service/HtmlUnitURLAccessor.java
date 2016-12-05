package com.turingdi.adpluto.service;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

/**
 * 基于HtmlUnit的实现
 */
public class HtmlUnitURLAccessor implements URLAccessor {
    //每个WebClient的使用次数限值
    private static final int WEBCLIENT_USE_TIMES = 6;
    //当前URLAccessor对象唯一的一个WebClient
    private WebClient webClient;
    //WebClient使用次数统计
    private int useCount = 0;

    private MissionConfig missionConfig;

    public HtmlUnitURLAccessor(MissionConfig missionConfig) {
        this.missionConfig = missionConfig;
        newWebClient();
    }

    private void newWebClient() {
        //创建一个CHROME浏览器Client
        webClient = new WebClient(BrowserVersion.FIREFOX_45);
        //设置webClient的相关参数
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);//必须加载JS，保证监控能够被执行
        webClient.getOptions().setCssEnabled(false);//不加载CSS，提高解析速度
        webClient.getOptions().setTimeout(30*1000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addRequestHeader("Accept", "*/*");
    }

    private String getRandomUserAgent() {
        return USER_AGENT[new Random(System.currentTimeMillis()).nextInt(USER_AGENT.length)];
    }

    private String getRandomReferer() {
        String[] referers = missionConfig.getReferer();
        return referers[new Random(System.currentTimeMillis()).nextInt(referers.length)];
    }

    @Override
    public void close(){
        webClient.close();
    }

    @Override
    public boolean accessURL(String clickURL) {
        checkWebClient();
        //按指定的比例，从Cookie存储对象中随机获取一个的旧的Cookie进行访问
        setRandomCookies();
        //按重试次数和访问成功的条件，确认继续访问还是退出
        for (int i = 0; i < RETRY_TIMES ; i++) {
            if(tryAccessURL(webClient, clickURL)){
                ProxyConfig curProxy = webClient.getOptions().getProxyConfig();
                Log4jUtils.getLogger().info("使用代理" + ProxyHolder.getProxyAddr(curProxy) +"访问" + clickURL + "成功......");
                return true;
            }
            try {
                Thread.sleep(RETRY_SLEEP * 1000);
            } catch (InterruptedException e) {
                //被shutdownNow()方法发出的interrupt()中断抛出异常
                return false;
            }
        }
        return false;
    }

    private void setRandomCookies() {
        Random rand = new Random(System.currentTimeMillis());
        webClient.getCookieManager().clearCookies();
        if (rand.nextFloat() > 1 / missionConfig.getBasic().getAdvPVAdvUV()) {
            Set<Cookie> sendCookie = CookiesStorer.getInstance().getRandomCookieSet();
            if (null != sendCookie) {
                for (Cookie cookie : sendCookie) {
                    webClient.getCookieManager().addCookie(cookie);
                }
            }
        }
    }

    /**
     * 检查WebClient对象使用次数是否到达回收阈值
     */
    private void checkWebClient() {
        useCount++;
        if(useCount >= WEBCLIENT_USE_TIMES){
            useCount = 0;
            webClient.close();
            newWebClient();
        }
    }

    private boolean tryAccessURL(WebClient webClient, String clickURL) {
        try {
            //每次访问切换代理
            changeProxy(webClient);
            webClient.addRequestHeader("User-Agent", getRandomUserAgent());
            webClient.addRequestHeader("Referer", getRandomReferer());
            //模拟浏览器打开一个目标网址
            HtmlPage page = webClient.getPage(clickURL);
            Log4jUtils.getLogger().info("打开的网页标题为：" + page.getTitleText());

            //模拟停留
            Thread.sleep(new Random().nextInt(2000)+2000);

            //模拟点击广告
            if(0 != missionConfig.getBasic().getImgClick()){
                clickImgElement(page);
            }

            // 取得cookie放入Cookie存储对象中
            Set<Cookie> receiveCookie = webClient.getCookieManager().getCookies();
            CookiesStorer.getInstance().addCookie(receiveCookie);
            return true;
        } catch ( FailingHttpStatusCodeException e) {
            ProxyConfig curProxy = webClient.getOptions().getProxyConfig();
            Log4jUtils.getLogger().error("使用代理" + ProxyHolder.getProxyAddr(curProxy) + "访问" + clickURL + "时返回错误码" + e.getStatusCode() + "，状态信息：" + e.getStatusMessage() + "，响应内容：" + e.getResponse().getContentAsString() + "。尝试重连......");
            if (e.getStatusCode() == 403) {
                ProxyHolder.getInstance().handleForbidden(webClient.getOptions().getProxyConfig());
            }
            return false;
        } catch (ConnectTimeoutException e) {
            ProxyConfig curProxy = webClient.getOptions().getProxyConfig();
            Log4jUtils.getLogger().error("使用代理" + ProxyHolder.getProxyAddr(curProxy) +"访问" + clickURL + "超时");
        } catch (IOException | InterruptedException e) {
            Log4jUtils.getLogger().error("",e);
        }
        return false;
    }

    private void clickImgElement(HtmlPage page) {
        Random rand = new Random(System.currentTimeMillis());
        MissionConfig.Basic basic = missionConfig.getBasic();
        double clickRatio = basic.getImgClick()/((double)basic.getTotaluv())/basic.getAdvPVAdvUV();
        if(rand.nextDouble() < clickRatio){
            for(DomElement imgEle : page.getElementsByTagName("img")){
                try {
                    imgEle.click();
                    Log4jUtils.getLogger().info("============>产生点击<============");
                } catch (IOException e) {
                    Log4jUtils.getLogger().error("模拟点击时发生异常，可能访问超时");
                }
            }
        }
    }

    private void changeProxy(WebClient webClient) {
        String area = missionConfig.getBasic().getArea();
        ProxyConfig anonymityProxy;
        //根据任务中有否设定地区而选择不同的代理
        if(null == area || area.length() == 0){
            anonymityProxy = ProxyHolder.getInstance().getRandomProxy();
        } else {
            anonymityProxy = ProxyHolder.getInstance().getRandomProxy(area);
        }
        if (null == anonymityProxy.getProxyHost()) {
            //拿不到代理的时候，增加IP伪造的HTTP头
            webClient.addRequestHeader("X-Forwarded-For", CommonUtils.getRandomIPAddr());
            webClient.addRequestHeader("Proxy-Client-IP", CommonUtils.getRandomIPAddr());
            webClient.addRequestHeader("WL-Proxy-Client-IP", CommonUtils.getRandomIPAddr());
            webClient.addRequestHeader("HTTP-Client-IP", CommonUtils.getRandomIPAddr());
        }
        webClient.getOptions().setProxyConfig(anonymityProxy);
        Log4jUtils.getLogger().info("正在使用代理" + ProxyHolder.getProxyAddr(anonymityProxy) + "进行访问。");
    }
}
