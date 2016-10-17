package com.turingdi.adpluto.service;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.turingdi.adpluto.utils.Log4jUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 存储Cookie
 *
 * Created by leibniz on 16-10-13.
 */
class CookiesStorer {
    private static CookiesStorer INSTANCE = new CookiesStorer();
    private static final Set<Set<Cookie>> cookiesSetSet = new HashSet<>();

    private CookiesStorer() {
    }

    static CookiesStorer getInstance() {
        return INSTANCE;
    }

    Set<Cookie> getRandomCookieSet() {
        return getRandomElemmentFromSet(cookiesSetSet);
    }

    private <T> T getRandomElemmentFromSet(Set<T> elementSet) {
        if (elementSet.size() <= 0) {
            return null;
        }
        int randIndex = new Random(System.currentTimeMillis()).nextInt(elementSet.size());
        int i = 0;
        for (T element : elementSet) {
            if (i == randIndex) {
                return element;
            }
            i++;
        }
        return null;
    }

    public void addCookie(Set<Cookie> cookies) {
        cookiesSetSet.add(cookies);
        Log4jUtils.getLogger().debug(cookiesSetSet);
    }
}
