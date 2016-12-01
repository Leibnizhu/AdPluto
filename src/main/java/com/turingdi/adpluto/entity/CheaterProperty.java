package com.turingdi.adpluto.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Created by leibniz on 16-11-29.
 */
public class CheaterProperty {
    //任务执行次数统计
    private volatile Integer browsedCount = 0;
    //失败任务统计
    private final Map<String, Integer> failedURL = new HashMap<>();

    public Integer getBrowsedCount() {
        return browsedCount;
    }

    public Map<String, Integer> getFailedURL() {
        return failedURL;
    }

    public synchronized void addBrowsedCount(){
        browsedCount++;
    }
}
