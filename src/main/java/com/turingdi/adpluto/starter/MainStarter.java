package com.turingdi.adpluto.starter;

import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.SystemConfig;
import com.turingdi.adpluto.rpc.NettyRPCServer;
import com.turingdi.adpluto.service.ProxyHolder;
import com.turingdi.adpluto.utils.Log4jUtils;
import com.turingdi.adpluto.utils.MySQLUtils;

public class MainStarter {


    public static void main(String[] args) {
        commonInit();
        try {
            if (args.length > 0 && "-server".equals(args[0])) {
                runCheatServer();
            } else {
                runCheatOnce();
            }
        } catch (Exception e) {
            Log4jUtils.getLogger().error("启动服务时抛出异常", e);
        }
        commonClose();
    }

    private static void commonClose() {
        MySQLUtils.closePool();//关闭MySQL连接池
    }

    private static void commonInit() {
        Log4jUtils.getLogger().info("已读取系统配置文件sys.json：" + SystemConfig.getInstance());//初始化系统基本配置
        MySQLUtils.initMySQLPool();//初始化MySQL连接池
        ProxyHolder.getInstance().getClass();//初始化本地IP代理池
    }

    private static void runCheatOnce() throws InterruptedException {
        Log4jUtils.getLogger().info("单次执行模式");
        MissionConfig jsonConfig= MissionConfig.parseFromDefaultFile();
        Log4jUtils.getLogger().info("已读取任务配置文件config.json并打乱：" + jsonConfig);//初始化作弊基本配置
        new Cheater(jsonConfig).startCheater();//启动作弊器
    }

    private static void runCheatServer() throws Exception {
        Log4jUtils.getLogger().info("RPC服务器模式");
        new NettyRPCServer().start();
    }
}
