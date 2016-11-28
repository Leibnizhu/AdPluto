package com.turingdi.adpluto.entity;

/*
 * Created by leibniz on 16-11-28.
 */
@SuppressWarnings("unused")
public class Proxy{
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
