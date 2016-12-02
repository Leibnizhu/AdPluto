package com.turingdi.adpluto.entity;

import com.gargoylesoftware.htmlunit.ProxyConfig;

/*
 * Created by leibniz on 16-11-28.
 */
@SuppressWarnings("unused")
public class Proxy extends ProxyConfig{
    //用于从Json中解析
    private String ip;
    private int port;
    private String area;

    public Proxy(ProxyConfig proxy) {
        super(proxy.getProxyHost(), proxy.getProxyPort(), proxy.isSocksProxy());
        this.ip = proxy.getProxyHost();
        this.port = proxy.getProxyPort();
    }

    public Proxy initSuper(){
        super.setProxyHost(ip);
        super.setProxyPort(port);
        return this;
    }

    public Proxy() {
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int hashCode() {
        return (super.getProxyHost()+":"+super.getProxyPort()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ProxyConfig))
            return false;
        ProxyConfig proxy = (ProxyConfig)obj;
        return super.getProxyHost().equals(proxy.getProxyHost())
                && super.getProxyPort() == proxy.getProxyPort()
                && super.isSocksProxy() == proxy.isSocksProxy();
    }
}
