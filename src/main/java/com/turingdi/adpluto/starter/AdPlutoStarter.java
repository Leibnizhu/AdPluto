package com.turingdi.adpluto.starter;

import com.turingdi.adpluto.entity.GlobalProperties;
import com.turingdi.adpluto.entity.GlobalProperties.Size;
import com.turingdi.adpluto.entity.RequestParams;
import com.turingdi.adpluto.service.DatabaseAccessor;
import com.turingdi.adpluto.service.URLAccessor;
import com.turingdi.adpluto.utils.Log4jUtils;
import com.turingdi.adpluto.utils.MySQLUtils;

import java.util.concurrent.*;

public class AdPlutoStarter {
    //线程池大小
    private static final int POOL_SIZE = 5;
    //任务队列最大长度
    private static final int QUEUE_MAX_LENGTH = 100;
    //线程池对象
    private ExecutorService threadPool;
    //任务队列
    private static final LinkedBlockingQueue<RequestParams> reqQueueStack = new LinkedBlockingQueue<>(QUEUE_MAX_LENGTH);

    public static void main(String[] args) throws InterruptedException {
        Log4jUtils.getLogger().info("已读取配置文件config.json：" + GlobalProperties.getGlobalProps());//初始化基本配置
        MySQLUtils.initMySQLPool();//初始化MySQL连接池
        AdPlutoStarter starter = new AdPlutoStarter();
        starter.startCheater();//启动作弊器
        MySQLUtils.closePool();//关闭MySQL连接池
    }

    private void startCheater() throws InterruptedException {
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
                                        //将任务压入任务队列
                                        while(!reqQueueStack.offer(req)){
                                            Thread.sleep(1000);
                                        }
                                        //需要触发的点击次数
                                        if (++clkCount >= totalPV) {
                                            closeThreadPool();
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

    private void closeThreadPool() throws InterruptedException {
        threadPool.shutdown();
        do{
            Log4jUtils.getLogger().info("等待线程池关闭...");
        } while(!threadPool.awaitTermination(5, TimeUnit.SECONDS));
        Log4jUtils.getLogger().info("线程池已关闭，正在退出...");
    }

    private void initThreadPool() {
        threadPool = Executors.newFixedThreadPool(POOL_SIZE);
        for(int i = 0; i < POOL_SIZE; i++){
            threadPool.submit(new Processor());
        }
    }

    private class Processor implements Callable<String>{
        private URLAccessor urlAccessor = new URLAccessor();

        @Override
        public String call() throws Exception {
            while(true){
                // 从队列弹出数据
                System.out.println("任务队列长度：" + reqQueueStack.size());
                if (reqQueueStack.size() > 0) {
                    RequestParams req = reqQueueStack.poll();
                    String clickURL = req.getClickURL();
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
            Log4jUtils.getLogger().info("当前线程处理完毕，准备关闭...");
            return "Success";
        }
    }
}
