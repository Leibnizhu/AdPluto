package com.turingdi.adpluto.service;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.turingdi.adpluto.entity.Proxy;
import com.turingdi.adpluto.entity.SystemConfig;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by leibniz on 16-11-25.
 */
public class ProxyHolder {
    private static final int ALLOW_FAIL_TIMES = 5;
    private static final ProxyConfig DIRECT_CONNECT = new ProxyConfig();//默认的直接连接的代理
    private static final Pattern PROVINCE_PATTERN = Pattern.compile("北京|天津|上海|重庆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|台湾|内蒙古|广西|西藏|宁夏|新疆|香港|澳门");
    private static ProxyHolder INSTANCE = new ProxyHolder();

    private Map<ProxyConfig, Integer> allProxyMap;//记录所有ProxyConfig及其失败次数
    private Map<String, Map<ProxyConfig, Integer>> areaProxyMapMap;//按地区记录所有ProxyConfig及其失败次数

    public static ProxyHolder getInstance() {
        return INSTANCE;
    }

    private ProxyHolder() {
        allProxyMap = new HashMap<>();
        areaProxyMapMap = new HashMap<>();
    }

    public void refreshProxysFromServer() {
        Log4jUtils.getLogger().info("开始抓取代理列表……");
        refreshProxyFromPythonSpider();
        refreshProxyFromPaidService();
        showProxyArea();
        Log4jUtils.getLogger().info("抓取代理列表完毕……");
    }

    private void refreshProxyFromPaidService() {
        String paidApiUrl = SystemConfig.getInstance().getPaidProxyGetApi();
        String proxyResult = CommonUtils.sendGetRequest(paidApiUrl);
        for(String line : proxyResult.split("\n")) {
            try {
                String[] proxyParams = line.split("[,:]");
                boolean isSock = proxyParams[2].toLowerCase().contains("sock");
                ProxyConfig proxyObj = new ProxyConfig(proxyParams[0], Integer.parseInt(proxyParams[1]), isSock);
                //加入到所有代理Map
                allProxyMap.put(proxyObj, 0);
                //加入到分地区的代理Map
                String area;
                if (proxyParams.length >= 5) {
                    area = proxyParams[4];
                } else {
                    area = proxyParams[3];
                }
                Map<ProxyConfig, Integer> areaProxyMap = areaProxyMapMap.computeIfAbsent(area, k -> new HashMap<>());
                areaProxyMap.put(proxyObj, 0);
            } catch(ArrayIndexOutOfBoundsException e){
                Log4jUtils.getLogger().error(line);
            }
        }
    }

    private void refreshProxyFromPythonSpider() {
        String proxyApiUrl = SystemConfig.getInstance().getSpiderProxyGetApi();
        String proxyJson = CommonUtils.sendGetRequest(proxyApiUrl);
        if(proxyJson.trim().length() > 0)ParseProxyJson(proxyJson);
    }

    private void showProxyArea() {
        Log4jUtils.getLogger().info("抓取到的代理总计：" + allProxyMap.size() + "个");
        areaProxyMapMap.forEach((province,proxyMap) -> Log4jUtils.getLogger().info("|" + province + "\t|" + proxyMap.size() + "\t|"));
    }

    private void ParseProxyJson(String proxyJson) {
        List<Proxy> result = JSON.parseArray(proxyJson, Proxy.class);
        for (Proxy proxy : result) {
            //加入到所有代理Map
            ProxyConfig proxyObj = new ProxyConfig(proxy.getIp(), proxy.getPort(), false);
            allProxyMap.put(proxyObj, 0);
            //设置完整地区信息
            String area = CommonUtils.unicodeDecode(proxy.getArea());
            proxy.setArea(area);
            //获取省份
            String province;
            if(area.length() > 0){
                Matcher match = PROVINCE_PATTERN.matcher(area);
                if (match.find()) {
                    //匹配到省份
                    province = match.group(0);
                } else {
                    province = "未知";
                }
            } else {
                province = "国际";
            }
            //加入到分地区的代理Map
            Map<ProxyConfig, Integer> areaProxyMap = areaProxyMapMap.computeIfAbsent(province, k -> new HashMap<>());
            areaProxyMap.put(proxyObj, 0);
        }
    }

    ProxyConfig getRandomProxy() {
        return getRandomProxy(allProxyMap);
    }

    ProxyConfig getRandomProxy(String area) {
        return getRandomProxy(areaProxyMapMap.get(area));
    }

    private ProxyConfig getRandomProxy(Map<ProxyConfig, Integer> proxyMap) {
        //代理池为空
        if (proxyMap.size() <= 0) {
            refreshProxysFromServer();
            return DIRECT_CONNECT;
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
        int failTime = allProxyMap.get(proxyConfig) + 1;
        allProxyMap.put(proxyConfig, failTime);
        if (failTime >= ALLOW_FAIL_TIMES) {
            Log4jUtils.getLogger().info("============>代理" + getProxyAddr(proxyConfig) + "失败次数过多，停止使用<============");
            allProxyMap.remove(proxyConfig);
            Log4jUtils.getLogger().info("============>从爬虫服务器删除代理返回结果：" + deleteProxyFromServer(proxyConfig) + "<============");
        }
    }

    private String deleteProxyFromServer(ProxyConfig proxyConfig) {
        //拼接调用删除接口的URL
        String originUrl = SystemConfig.getInstance().getSpiderProxyDelApi();
        String proxyApiUrl = originUrl.replaceAll("<ip>", proxyConfig.getProxyHost()).replaceAll("<port>", String.valueOf(proxyConfig.getProxyPort()));
        return CommonUtils.sendGetRequest(proxyApiUrl);
    }

    static String getProxyAddr(ProxyConfig proxy) {
        return proxy.getProxyHost() + ":" + proxy.getProxyPort();
    }
}