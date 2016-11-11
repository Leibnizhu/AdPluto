package com.turingdi.adpluto.service;

import java.util.Random;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.turingdi.adpluto.entity.GlobalProperties;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;

public class URLAccessor {
    //连接重试的次数
    private static final int RETRY_TIMES = 3;
    //连接重试之前的等待时间，秒
    private static final int RETRY_SLEEP = 10;
    //每个WebClient的使用次数限值
    private static final int WEBCLIENT_USE_TIMES = 6;
    //当前URLAccessor对象唯一的一个WebClient
    private WebClient webClient;
    //WebClient使用次数统计
    private int useCount = 0;

    public URLAccessor() {
        newWebClient();
    }

    private void newWebClient() {
        //创建一个CHROME浏览器Client
        webClient = new WebClient(BrowserVersion.FIREFOX_38);
        //设置webClient的相关参数
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);//必须加载JS，保证监控能够被执行
        webClient.getOptions().setCssEnabled(false);//不加载CSS，提高解析速度
        webClient.getOptions().setTimeout(35000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addRequestHeader("X-Forwarded-For", CommonUtils.getRandomIPAddr());
        webClient.addRequestHeader("Proxy-Client-IP", CommonUtils.getRandomIPAddr());
        webClient.addRequestHeader("WL-Proxy-Client-IP", CommonUtils.getRandomIPAddr());
        webClient.addRequestHeader("HTTP-Client-IP", CommonUtils.getRandomIPAddr());
    }

    public boolean accessURL(String clickURL) {
        checkWebClient();
        Random rand = new Random(System.currentTimeMillis());
        //按指定的比例，从Cookie存储对象中随机获取一个的旧的Cookie进行访问
        webClient.getCookieManager().clearCookies();
        if (rand.nextFloat() > 1 / GlobalProperties.getGlobalProps().getBasic().getAdvPVAdvUV()) {
            Set<Cookie> sendCookie = CookiesStorer.getInstance().getRandomCookieSet();
            if (null != sendCookie) {
                for (Cookie cookie : sendCookie) {
                    webClient.getCookieManager().addCookie(cookie);
                }
            }
        }
        //按重试次数和访问成功的条件，确认继续访问还是退出
        boolean result = false;
        for (int i = 0; i < RETRY_TIMES && !result; i++) {
            result = tryAccessURL(webClient, clickURL);
            try {
                Thread.sleep(RETRY_SLEEP * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
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
            //模拟浏览器打开一个目标网址
            HtmlPage page = webClient.getPage(clickURL);
            Log4jUtils.getLogger().info("打开的网页标题为：" + page.getTitleText());

            // 取得cookie放入Cookie存储对象中
            Set<Cookie> receiveCookie = webClient.getCookieManager().getCookies();
            CookiesStorer.getInstance().addCookie(receiveCookie);
            return true;
        } catch (Exception e) {
            Log4jUtils.getLogger().error("创建访问" + clickURL + "时抛出异常，尝试重连......", e);
            return false;
        }
    }
}
