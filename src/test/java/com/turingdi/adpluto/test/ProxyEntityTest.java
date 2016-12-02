package com.turingdi.adpluto.test;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.turingdi.adpluto.entity.Proxy;
import com.turingdi.adpluto.service.ProxyHolder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leibniz on 16-12-1.
 */
public class ProxyEntityTest {
    @Test
    public void testHashcode(){
        Proxy p1 = new Proxy(new ProxyConfig("12.12.12.12", 111, false));
        Proxy p2 = new Proxy(new ProxyConfig("12.12.12.12", 111, false));
        Proxy p3 = new Proxy(new ProxyConfig("12.12.12.13", 111, false));
        System.out.println(p1.hashCode() + "\n" + p2.hashCode() + "\n" + p3.hashCode());
    }

    @Test
    public void testEquals(){
        Proxy p1 = new Proxy(new ProxyConfig("12.12.12.12", 111, false));
        Proxy p2 = new Proxy(new ProxyConfig("12.12.12.12", 111, false));
        Proxy p3 = new Proxy(new ProxyConfig("12.12.12.13", 111, false));
        System.out.println(p1.equals(p2) + "\n" + p2.equals(p1) + "\n" + p3.equals(p1));
    }

    @Test
    public void testHashMap(){
        Proxy p1 = new Proxy(new ProxyConfig("12.12.12.12", 111, false));
        Proxy p2 = new Proxy(new ProxyConfig("12.12.12.12", 111, false));
        Proxy p3 = new Proxy(new ProxyConfig("12.12.12.13", 111, false));
        Map<ProxyConfig, Integer> map = new HashMap<>();
        map.put(p1, 1);
        map.put(p2, 1);
        map.put(p3, 1);
        System.out.println(map.size());
    }

    @Test
    public void testLoadProxyFromServer(){
        ProxyHolder.getInstance().refreshProxysFromServer();
        ProxyHolder.getInstance().refreshProxysFromServer();
        ProxyHolder.getInstance().refreshProxysFromServer();

    }
}
