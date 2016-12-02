package com.turingdi.adpluto.starter;

import com.turingdi.adpluto.cheat.Cheater;
import com.turingdi.adpluto.cheat.MissionManager;
import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.rpc.NettyRPCServer;
import com.turingdi.adpluto.utils.Log4jUtils;

public class MainStarter {


    public static void main(String[] args) {
        MissionManager.initialize();
        if (args.length > 0 && "-server".equals(args[0])) {
            runCheatServer();
        } else {
            runCheatOnce();
        }
        MissionManager.destroy();
    }


    private static void runCheatOnce() {
        Log4jUtils.getLogger().info("单次执行模式");
        MissionConfig jsonConfig= MissionConfig.parseFromDefaultFile();
        if(jsonConfig != null && jsonConfig.checkMissionValid()){
            Log4jUtils.getLogger().info("已读取任务配置文件config.json并打乱：" + jsonConfig);
            new Cheater(jsonConfig, "RunOnce").start();//启动作弊器
        } else {
            Log4jUtils.getLogger().error("已读取任务配置文件config.json，文件内容不全，请检查修复后重试");
        }
    }

    private static void runCheatServer() {
        Log4jUtils.getLogger().info("RPC服务器模式");
        new NettyRPCServer().start();
    }
}
