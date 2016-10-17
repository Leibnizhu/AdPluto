package com.turingdi.adpluto.starter;

import com.turingdi.adpluto.entity.GlobalProperties;
import com.turingdi.adpluto.entity.GlobalProperties.Size;
import com.turingdi.adpluto.entity.RequestParams;
import com.turingdi.adpluto.service.DatabaseAccessor;
import com.turingdi.adpluto.service.URLAccessor;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;
import com.turingdi.adpluto.utils.MySQLUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AdPlutoStarter {
    //线程池大小
    private static final int POOL_SIZE = 5;
    //任务队列最大长度
    private static final int QUEUE_MAX_LENGTH = 100;
    private ExecutorService threadPool;
    public static final LinkedBlockingQueue<RequestParams> reqQueueStack = new LinkedBlockingQueue<>(QUEUE_MAX_LENGTH);

    public static void main(String[] args) {
        Log4jUtils.getLogger().info("已读取配置文件config.json：" + GlobalProperties.getGlobalProps());//初始化基本配置
        MySQLUtils.initMySQLPool();//初始化MySQL连接池
        AdPlutoStarter starter = new AdPlutoStarter();
        starter.startCheater();//启动作弊器
        while(!starter.threadPool.isTerminated() || !starter.threadPool.isTerminated()){
        }
        MySQLUtils.closePool();//关闭MySQL连接池
    }

    private void startCheater() {
        initThreadPool();//初始化连接池
        GlobalProperties props = GlobalProperties.getGlobalProps();
        int totalPV = (int) (props.getBasic().getTotaluv() * props.getBasic().getAdvPVAdvUV());
        int clkCount = 0;//统计当前执行了多少次广告主落地页的PV
        //遍历所有可能的宏替换排列组合
        while (clkCount < totalPV) {
            for (GlobalProperties.Campaign camp : props.getCamp()) {
                for (Size size : props.getSize()) {
                    for (String campid : camp.getCampid()) {
                        for (String crtvPkgId : props.getCtid()) {
                            for (String adzoneId : props.getSpotid()) {
                                for (String tag : props.getTag()) {
                                    for (String clickURL : props.getUrl()) {
                                        RequestParams req = new RequestParams(clickURL, camp.getAdxid(), size, crtvPkgId, campid, adzoneId, tag);
                                        reqQueueStack.offer(req); //将任务压入任务队列
                                        //需要触发的点击次数
                                        if (++clkCount >= totalPV) {
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

    private void initThreadPool() {
        threadPool = Executors.newFixedThreadPool(POOL_SIZE);
        for(int i = 0; i < POOL_SIZE; i++){
            threadPool.execute(new Processor());
        }
    }

    private class Processor implements Runnable{
        private URLAccessor urlAccessor = new URLAccessor();

        @Override
        public void run() {
            RequestParams req = null;
            String clickURL = null;
            while(true){
                // 从队列弹出数据
                System.out.println("任务队列长度：" + reqQueueStack.size());
                if (reqQueueStack.size() > 0) {
                    req = reqQueueStack.poll();
                    clickURL = req.getClickURL();
                    Log4jUtils.getLogger().info("实际访问URL：" + clickURL);
                    //按指定的PVUV比例访问URL
                    if(urlAccessor.accessURL(clickURL)){
                        //写入扒数平台的MySQL
                        DatabaseAccessor.getInstance().incrDataBase(req);
                    }
                } else {
                    try {
                        Thread.sleep(5000);
                        if(reqQueueStack.size() == 0){
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
