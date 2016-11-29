package com.turingdi.adpluto.starter;

import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.RequestParams;
import com.turingdi.adpluto.entity.CheaterProperty;
import com.turingdi.adpluto.service.DatabaseAccessor;
import com.turingdi.adpluto.service.URLAccessor;
import com.turingdi.adpluto.utils.Log4jUtils;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Created by leibniz on 16-11-29.
 */
public class CheaterThread implements Callable<String> {
    private URLAccessor urlAccessor;
    private CheaterProperty cheatProps;
    private MissionConfig missionConfig;

    CheaterThread(CheaterProperty cheatProps, MissionConfig missionConfig) {
        this.cheatProps = cheatProps;
        this.missionConfig = missionConfig;
        urlAccessor = new URLAccessor(missionConfig);
    }

    @Override
    public String call() throws Exception {
        LinkedBlockingQueue<RequestParams> reqQueueStack = cheatProps.getReqQueueStack();
        Map<String, Integer> failedURL = cheatProps.getFailedURL();
        Thread.sleep(5000);
        while (true) {
            // 从队列弹出数据
            Log4jUtils.getLogger().info("任务队列长度：" + reqQueueStack.size());
            if (reqQueueStack.size() > 0) {
                try {
                    RequestParams req = reqQueueStack.poll();
                    cheatProps.addBrowsedCount();
                    int curCount = cheatProps.getBrowsedCount();
                    Log4jUtils.getLogger().info("执行第" + curCount + "次任务");
                    String clickURL = req.getClickURL();
                    Log4jUtils.getLogger().info("实际访问URL：" + clickURL);
                    //按指定的PVUV比例访问URL
                    if (urlAccessor.accessURL(clickURL)) {
                        if (req.getMysqlAdxId() != null) {
                            //写入扒数平台的MySQL
                            DatabaseAccessor.getInstance().incrDataBase(req, missionConfig);
                        } else {
                            Log4jUtils.getLogger().info("本次访问无需写入Data Optimus数据库");
                        }
                        Log4jUtils.getLogger().info("============>第" + curCount + "次任务已完成<============");
                    } else {
                        Integer failTime = failedURL.get(clickURL);
                        failTime = null == failTime ? 0 : failTime;
                        failedURL.put(clickURL, failTime + 1);
                        Log4jUtils.getLogger().info("============>第" + curCount + "次任务失败<============");
                    }
                } catch (Exception ignored) {
                }
            } else {
                Thread.sleep(10000);
                if (reqQueueStack.size() == 0) {
                    break;
                }
            }
        }
        Log4jUtils.getLogger().info("线程" + Thread.currentThread() + "处理完毕，准备关闭...");
        return "Success";
    }
}
