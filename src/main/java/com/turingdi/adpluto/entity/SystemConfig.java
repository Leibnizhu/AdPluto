package com.turingdi.adpluto.entity;

import com.alibaba.fastjson.JSON;
import com.turingdi.adpluto.utils.Log4jUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 保存用于刷流量的全局配置
 *
 * @author leibniz 2016-10-12 15:23
 */
public class SystemConfig {

    private String JDBCDriver; //驱动类名
    private String url;//MySQL数据库地址
    private String userId;//用户名
    private String password;//密码
    private int serverPort;//作为服务器的时候使用的端口
    private int initConns;//连接池初始化连接数
    private int maxConns;//连接池最大连接数
    private String spiderProxyGetApi;//自己的爬虫代理的获取API
    private String spiderProxyDelApi;//自己的爬虫代理的删除API
    private String paidProxyGetApi;//付费代理的获取API
    private Map<String, String> adxidMapping;//DSP业务平台中的ADX ID和扒数平台中ADX ID的映射关系

    private static SystemConfig INSTANCE;

    static {
        InputStream is = null;
        try {
            is = new BufferedInputStream(SystemConfig.class.getResourceAsStream("/sys.json"));
            INSTANCE = JSON.parseObject(is, SystemConfig.class);
        } catch (IOException e) {
            Log4jUtils.getLogger().error("读取sys.json配置文件时抛出IO异常", e);
        } finally {
            if (null != is) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SystemConfig getInstance() {
        return INSTANCE;
    }


    @Override
    public String toString() {
        return "SystemConfig{" +
                "JDBCDriver='" + JDBCDriver + '\'' +
                ", url='" + url + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", serverPort=" + serverPort +
                ", initConns=" + initConns +
                ", maxConns=" + maxConns +
                ", spiderProxyGetApi='" + spiderProxyGetApi + '\'' +
                ", spiderProxyDelApi='" + spiderProxyDelApi + '\'' +
                ", paidProxyGetApi='" + paidProxyGetApi + '\'' +
                ", adxidMapping=" + adxidMapping +
                '}';
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getSpiderProxyGetApi() {
        return spiderProxyGetApi;
    }

    public void setSpiderProxyGetApi(String spiderProxyGetApi) {
        this.spiderProxyGetApi = spiderProxyGetApi;
    }

    public String getSpiderProxyDelApi() {
        return spiderProxyDelApi;
    }

    public void setSpiderProxyDelApi(String spiderProxyDelApi) {
        this.spiderProxyDelApi = spiderProxyDelApi;
    }

    public String getPaidProxyGetApi() {
        return paidProxyGetApi;
    }

    public void setPaidProxyGetApi(String paidProxyGetApi) {
        this.paidProxyGetApi = paidProxyGetApi;
    }

    public Map<String, String> getAdxidMapping() {
        return adxidMapping;
    }

    @SuppressWarnings("unused")
    public SystemConfig setAdxidMapping(Map<String, String> adxidMapping) {
        this.adxidMapping = adxidMapping;
        return this;
    }

    public String getJDBCDriver() {
        return JDBCDriver;
    }

    @SuppressWarnings("unused")
    public SystemConfig setJDBCDriver(String JDBCDriver) {
        this.JDBCDriver = JDBCDriver;
        return this;
    }

    public String getUrl() {
        return url;
    }

    @SuppressWarnings("unused")
    public SystemConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public SystemConfig setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public SystemConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getInitConns() {
        return initConns;
    }

    @SuppressWarnings("unused")
    public SystemConfig setInitConns(int initConns) {
        this.initConns = initConns;
        return this;
    }

    public int getMaxConns() {
        return maxConns;
    }

    @SuppressWarnings("unused")
    public SystemConfig setMaxConns(int maxConns) {
        this.maxConns = maxConns;
        return this;
    }
}
