package com.turingdi.adpluto.service;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.turingdi.adpluto.utils.Log4jUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by leibniz on 16-10-13.
 */
public class CookiesStorer {
    private static CookiesStorer INSTANCE = new CookiesStorer();
    private static final Set<String> cookiesStrSet = new HashSet<String>();
    private static final Set<Set<Cookie>> cookiesSetSet = new HashSet<>();

    private CookiesStorer() {
    }

    public static CookiesStorer getInstance() {
        return INSTANCE;
    }

    public String getRandomCookieStr() {
        return getRandomElemmentFromSet(cookiesStrSet);
    }

    public Set<Cookie> getRandomCookieSet() {
        return getRandomElemmentFromSet(cookiesSetSet);
    }

    private <T> T getRandomElemmentFromSet(Set<T> elementSet){
        if(elementSet.size() <=0 ){
            return null;
        }
        int randIndex = new Random(System.currentTimeMillis()).nextInt(elementSet.size());
        int i = 0;
        for(T element : elementSet){
            if(i == randIndex){
                return element;
            }
            i++;
        }
        return null;
    }

    public void addCookie(String cookie) {
        cookiesStrSet.add(cookie);
        //Log4jUtils.getLogger().debug(cookiesStrSet);
    }

    public void addCookie(Set<Cookie> cookies) {
        cookiesSetSet.add(cookies);
        Log4jUtils.getLogger().debug(cookiesSetSet);
    }
}
