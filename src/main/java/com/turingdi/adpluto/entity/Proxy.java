package com.turingdi.adpluto.entity;

/*
 * Created by leibniz on 16-11-28.
 */
@SuppressWarnings("unused")
public class Proxy {
    private String ip;
    private int port;
    private String area;

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
}
