package com.turingdi.adpluto.starter;

import com.turingdi.adpluto.entity.CheaterProperty;
import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.RequestParams;
import com.turingdi.adpluto.utils.Log4jUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Created by leibniz on 16-11-29.
 */
class Cheater {
    //线程池大小
    private static final int POOL_SIZE = 5;
    //线程池对象
    private ExecutorService threadPool;
    //刷UV配置
    private MissionConfig config;

    Cheater(MissionConfig config) {
        this.config = config;
    }

    void startCheater() throws InterruptedException {
        CheaterProperty cheatProps = new CheaterProperty();
        registerShutdownHook(cheatProps);//注册程序关闭监听钩子
        initThreadPool(cheatProps, config);//初始化连接池
        int totalPV = (int) (config.getBasic().getTotaluv() * config.getBasic().getAdvPVAdvUV());
        int clkCount = 0;//统计当前执行了多少次广告主落地页的PV
        //遍历所有可能的宏替换排列组合
        while (clkCount < totalPV) {
            for (MissionConfig.Campaign camp : config.getCamp()) {
                for (MissionConfig.Size size : config.getSize()) {
                    for (String campid : camp.getCampid()) {
                        for (String crtvPkgId : config.getCtid()) {
                            for (String adzoneId : config.getSpotid()) {
                                for (String tag : config.getTag()) {
                                    for (String clickURL : config.getUrl()) {
                                        RequestParams req = new RequestParams(clickURL, camp.getAdxid(), size, crtvPkgId, campid, adzoneId, tag);
                                        //将任务压入任务队列
                                        while(!cheatProps.getReqQueueStack().offer(req)){
                                            Thread.sleep(1000);
                                        }
                                        //需要触发的点击次数
                                        if (++clkCount >= totalPV) {
                                            closeThreadPool(cheatProps);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void registerShutdownHook(CheaterProperty setting) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Log4jUtils.getLogger().info("强制退出ing……失败的任务有(显示格式：访问失败任务的URL=失败次数)：\n" + setting.getFailedURL())));
    }

    private void initThreadPool(CheaterProperty cheatProps, MissionConfig config) {
        threadPool = Executors.newFixedThreadPool(POOL_SIZE);
        for(int i = 0; i < POOL_SIZE; i++){
            threadPool.submit(new CheaterThread(cheatProps, config));
        }
    }

    private void closeThreadPool(CheaterProperty setting) throws InterruptedException {
        threadPool.shutdown();
        while(!threadPool.awaitTermination(10, TimeUnit.SECONDS)){
            Log4jUtils.getLogger().info("等待线程池关闭...");
        }
        Log4jUtils.getLogger().info("失败的任务有：" + setting.getFailedURL());
        Log4jUtils.getLogger().info("线程池已关闭，正在退出...");
    }
}
