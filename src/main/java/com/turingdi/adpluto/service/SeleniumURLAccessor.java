package com.turingdi.adpluto.service;

/**
 * 基于Selenium的实现
 * Created by leibniz on 16-12-5.
 */
public class SeleniumURLAccessor implements URLAccessor{
    @Override
    public void close() {

    }

    @Override
    public boolean accessURL(String clickURL) {
        return false;
    }
}
