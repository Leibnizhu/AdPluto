package com.turingdi.adpluto.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.turingdi.adpluto.entity.GlobalProperties;
import com.turingdi.adpluto.utils.Log4jUtils;

public class URLAccessor {

    private static URLAccessor INSTANCE = new URLAccessor();

    private URLAccessor() {
    }

    public static URLAccessor getInstance() {
        return INSTANCE;
    }

    public void accessURL(String clickURL, Random rand) {
        try {
            //创建一个CHROME浏览器Client
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            //设置webClient的相关参数
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);//不加载CSS，提高解析速度
            webClient.getOptions().setTimeout(35000);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            //按指定的比例，从Cookie存储对象中随机获取一个的旧的Cookie进行访问
            if (rand.nextFloat() > 1 / GlobalProperties.getGlobalProps().getBasic().getAdvPVAdvUV()) {
                Set<Cookie> sendCookie = CookiesStorer.getInstance().getRandomCookieSet();
                if (null != sendCookie) {
                    for (Cookie cookie : sendCookie) {
                        webClient.getCookieManager().addCookie(cookie);
                    }
                }
            }
            //模拟浏览器打开一个目标网址
            HtmlPage page = webClient.getPage(clickURL);
            System.out.println(page.getTitleText());

            // 取得cookie放入Cookie存储对象中
            Set<Cookie> receiveCookie = webClient.getCookieManager().getCookies();
            CookiesStorer.getInstance().addCookie(receiveCookie);
        } catch (IOException e) {
            Log4jUtils.getLogger().error("创建URL对象和连接时抛出异常", e);
        }
    }
}
