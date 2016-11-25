package com.turingdi.adpluto.service;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.turingdi.adpluto.entity.GlobalProperties;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 * Created by leibniz on 16-11-25.
 */
public class ProxyHolder {
    private static ProxyHolder INSTANCE = new ProxyHolder();
    private static Map<ProxyConfig, Integer> proxyMap;//记录ProxyConfig及其失败次数
    private static final int ALLOW_FAIL_TIMES = 5;

    public static ProxyHolder getInstance() {
        return INSTANCE;
    }

    private ProxyHolder() {
        Log4jUtils.getLogger().info("开始抓取代理列表……");
        String proxyApiUrl = GlobalProperties.getGlobalProps().getMysql().getProxyApi()+"//?types=0";
        String proxyJson = CommonUtils.sendGetRequest(proxyApiUrl);
        ParseProxyJson(proxyJson);
        Log4jUtils.getLogger().info("抓取代理列表完毕……");
    }

    private void ParseProxyJson(String proxyJson) {
        List<Proxy> result = JSON.parseArray(proxyJson, Proxy.class);
        proxyMap = new HashMap<>();
        for(Proxy proxy : result){
            proxyMap.put(new ProxyConfig(proxy.getIp(), proxy.getPort(),false), 0);
        }
    }

    ProxyConfig getRandomProxy() {
        if (proxyMap.size() <= 0) {
            return null;
        }
        int randIndex = new Random(System.currentTimeMillis()).nextInt(proxyMap.size());
        int i = 0;
        for (ProxyConfig proxy : proxyMap.keySet()) {
            if (i == randIndex) {
                return proxy;
            }
            i++;
        }
        return null;
    }

    void handleForbidden(ProxyConfig proxyConfig) {
        int failTime = proxyMap.get(proxyConfig)+1;
        proxyMap.put(proxyConfig, failTime);
        if(failTime >= ALLOW_FAIL_TIMES){
            Log4jUtils.getLogger().info("============>代理" + proxyConfig.getProxyHost() + ":" + proxyConfig.getProxyPort() + "失败次数过多，停止使用<============");
            proxyMap.remove(proxyConfig);
            Log4jUtils.getLogger().info("============>从爬虫服务器删除代理返回结果：" + deleteProxyFromServer(proxyConfig) + "<============");
        }
    }

    private String deleteProxyFromServer(ProxyConfig proxyConfig) {
        //拼接调用删除接口的URL
        String proxyApiUrl = new StringBuffer()
                .append(GlobalProperties.getGlobalProps().getMysql().getProxyApi())
                .append("/?delete=true&ip=")
                .append(proxyConfig.getProxyHost())
                .append("&port=")
                .append(proxyConfig.getProxyPort())
                .toString();
        return CommonUtils.sendGetRequest(proxyApiUrl);
    }
}

@SuppressWarnings("unused")
class Proxy{
    private String ip;
    private Integer port;

    public Proxy(){
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
}
