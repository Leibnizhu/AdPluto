package com.turingdi.adpluto.cheat;

import com.turingdi.adpluto.entity.CheaterProperty;
import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.RequestParams;
import com.turingdi.adpluto.utils.Log4jUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Created by leibniz on 16-11-29.
 */
public class Cheater {
    //线程池大小
    private static final int POOL_SIZE = 5;
    //线程池对象
    private ExecutorService threadPool;
    //刷UV配置
    private MissionConfig config;
    //Cheater配置
    private  CheaterProperty cheatProps;
    //状态
    enum CHEATER_STATUS {
        ADDED, STARTED, ERROR, ENDED
    }
    private CHEATER_STATUS status;

    CHEATER_STATUS getStatus() {
        return status;
    }

    public Cheater(MissionConfig config) {
        status = CHEATER_STATUS.ADDED;
        this.config = config;
        cheatProps = new CheaterProperty();
        registerShutdownHook(cheatProps);//注册程序关闭监听钩子
        initThreadPool(cheatProps, config);//初始化连接池
    }

    void forceStop(){
        threadPool.shutdownNow();
        status = CHEATER_STATUS.ENDED;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    status = CHEATER_STATUS.STARTED;
                    int totalPV = (int) (config.getBasic().getTotaluv() * config.getBasic().getAdvPVAdvUV());
                    //遍历所有可能的宏替换排列组合
                    List<RequestParams> reqParamList = getReqParamsList();
                    for (int clkCount = 0; clkCount < totalPV; clkCount++) {
                        RequestParams req = reqParamList.get(clkCount % reqParamList.size());
                        //将任务压入任务队列
                        while (!cheatProps.getReqQueueStack().offer(req)) {
                            Thread.sleep(1000);
                        }
                        //需要触发的点击次数
                        if (++clkCount >= totalPV) {
                            closeThreadPool(cheatProps);
                            return;
                        }
                    }
                } catch (InterruptedException e) {
                    status = CHEATER_STATUS.ERROR;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //根据现有的多个条件，生成乱序的待访问请求参数
    private List<RequestParams> getReqParamsList(){
        List<RequestParams> result = new ArrayList<>();
        for (MissionConfig.Campaign camp : config.getCamp()) {
            for (MissionConfig.Size size : config.getSize()) {
                for (String campid : camp.getCampid()) {
                    for (String crtvPkgId : config.getCtid()) {
                        for (String adzoneId : config.getSpotid()) {
                            for (String tag : config.getTag()) {
                                for (String clickURL : config.getUrl()) {
                                    result.add(new RequestParams(clickURL, camp.getAdxid(), size, crtvPkgId, campid, adzoneId, tag));
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.shuffle(result);//打乱顺序
        return result;
    }

    private void registerShutdownHook(CheaterProperty setting) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Log4jUtils.getLogger().info("退出程序ing……失败的任务有(显示格式：访问失败任务的URL=失败次数)：\n" + setting.getFailedURL())));
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
        status = CHEATER_STATUS.ENDED;
        Log4jUtils.getLogger().info("线程池已关闭，正在退出...");
    }
}
