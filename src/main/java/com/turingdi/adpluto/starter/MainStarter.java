package com.turingdi.adpluto.starter;

import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.MissionConfig.Size;
import com.turingdi.adpluto.entity.RequestParams;
import com.turingdi.adpluto.entity.CheaterSetting;
import com.turingdi.adpluto.entity.SystemConfig;
import com.turingdi.adpluto.rpc.NettyRPCServer;
import com.turingdi.adpluto.service.ProxyHolder;
import com.turingdi.adpluto.utils.Log4jUtils;
import com.turingdi.adpluto.utils.MySQLUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainStarter {
    //线程池大小
    private static final int POOL_SIZE = 5;
    //线程池对象
    private ExecutorService threadPool;

    public static void main(String[] args) {
        basicInit();
        try {
            if (args.length > 0 && "-server".equals(args[0])) {
                runCheatServer();
            } else {
                runCheatOnce();
            }
        } catch (Exception e) {
            Log4jUtils.getLogger().error("启动服务时抛出异常", e);
        }
    }

    private static void basicInit() {
        Log4jUtils.getLogger().info("已读取系统配置文件sys.json：" + SystemConfig.getInstance());//初始化系统基本配置
        MySQLUtils.initMySQLPool();//初始化MySQL连接池
        ProxyHolder.getInstance().getClass();//初始化本地IP代理池
    }

    private static void runCheatOnce() throws InterruptedException {
        Log4jUtils.getLogger().info("单次执行模式");
        Log4jUtils.getLogger().info("已读取任务配置文件config.json：" + MissionConfig.getGlobalProps());//初始化作弊基本配置
        MissionConfig.getGlobalProps().shuffle();//打乱配置
        MainStarter starter = new MainStarter();
        starter.startCheater();//启动作弊器
        MySQLUtils.closePool();//关闭MySQL连接池
    }

    private static void runCheatServer() throws Exception {
        Log4jUtils.getLogger().info("RPC服务器模式");
        new NettyRPCServer().start();
    }

    private void startCheater() throws InterruptedException {
        CheaterSetting setting = new CheaterSetting();
        registerShutdownHook(setting);//注册程序关闭监听钩子
        initThreadPool(setting);//初始化连接池
        MissionConfig props = MissionConfig.getGlobalProps();
        int totalPV = (int) (props.getBasic().getTotaluv() * props.getBasic().getAdvPVAdvUV());
        int clkCount = 0;//统计当前执行了多少次广告主落地页的PV
        //遍历所有可能的宏替换排列组合
        while (clkCount < totalPV) {
            for (MissionConfig.Campaign camp : props.getCamp()) {
                for (Size size : props.getSize()) {
                    for (String campid : camp.getCampid()) {
                        for (String crtvPkgId : props.getCtid()) {
                            for (String adzoneId : props.getSpotid()) {
                                for (String tag : props.getTag()) {
                                    for (String clickURL : props.getUrl()) {
                                        RequestParams req = new RequestParams(clickURL, camp.getAdxid(), size, crtvPkgId, campid, adzoneId, tag);
                                        //将任务压入任务队列
                                        while(!setting.getReqQueueStack().offer(req)){
                                            Thread.sleep(1000);
                                        }
                                        //需要触发的点击次数
                                        if (++clkCount >= totalPV) {
                                            closeThreadPool(setting);
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

    private void registerShutdownHook(CheaterSetting setting) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Log4jUtils.getLogger().info("强制退出ing……失败的任务有(显示格式：访问失败任务的URL=失败次数)：\n" + setting.getFailedURL())));
    }

    private void initThreadPool(CheaterSetting setting) {
        threadPool = Executors.newFixedThreadPool(POOL_SIZE);
        for(int i = 0; i < POOL_SIZE; i++){
            threadPool.submit(new CheaterThread(setting));
        }
    }

    private void closeThreadPool(CheaterSetting setting) throws InterruptedException {
        threadPool.shutdown();
        while(!threadPool.awaitTermination(10, TimeUnit.SECONDS)){
            Log4jUtils.getLogger().info("等待线程池关闭...");
        }
        Log4jUtils.getLogger().info("失败的任务有：" + setting.getFailedURL());
        Log4jUtils.getLogger().info("线程池已关闭，正在退出...");
    }
}
