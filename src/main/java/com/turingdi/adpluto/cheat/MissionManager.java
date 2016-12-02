package com.turingdi.adpluto.cheat;

import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.MissionResponse;
import com.turingdi.adpluto.entity.SystemConfig;
import com.turingdi.adpluto.service.ProxyHolder;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;
import com.turingdi.adpluto.utils.MySQLUtils;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by leibniz on 16-11-30.
 */
public class MissionManager {
    private static MissionManager INSTANCE = new MissionManager();
    private final Map<String, Cheater> missionMap = new HashMap<>();

    public static MissionManager getInstance() {
        return INSTANCE;
    }

    private MissionManager() {
    }

    public MissionResponse addOneMission(MissionConfig missionConfig) {
        switch (missionConfig.getMission().getType()) {
            case 1:
                return newCheatMission(missionConfig);
            case 2:
                return checkMissionStatus(missionConfig);
            case 3:
                return forceBreakMission(missionConfig);
            default:
                return unvalidMissionType();
        }
    }

    private MissionResponse unvalidMissionType() {
        return new MissionResponse("", "Unvalid mission type!");
    }

    private MissionResponse newCheatMission(MissionConfig missionConfig) {
        if(missionConfig.checkMissionValid()){
            String cheaterID = CommonUtils.getRandomID();
            Cheater cheater = new Cheater(missionConfig, cheaterID);
            cheater.start();
            missionMap.put(cheaterID, cheater);
            return new MissionResponse(cheaterID, "Successfully Add a Mission");
        } else {
            return new MissionResponse("", "Unvalid mission content, lack of some information!");
        }
    }

    private MissionResponse checkMissionStatus(MissionConfig missionConfig) {
        String cheaterID = String.valueOf(missionConfig.getMission().getId());
        String status = missionMap.get(cheaterID).getStatus().toString();
        return new MissionResponse(cheaterID, status);
    }

    private MissionResponse forceBreakMission(MissionConfig missionConfig) {
        String cheaterID = String.valueOf(missionConfig.getMission().getId());
        missionMap.get(cheaterID).forceStop();
        return new MissionResponse(cheaterID, "Succes Stop Cheater");
    }

    /**
     * 初始化方法和销毁方法
     */

    public static void destroy() {
        MySQLUtils.closePool();//关闭MySQL连接池
    }

    public static void initialize() {
        Log4jUtils.getLogger().info("已读取系统配置文件sys.json：" + SystemConfig.getInstance());//初始化系统基本配置
        MySQLUtils.initMySQLPool();//初始化MySQL连接池
        ProxyHolder.getInstance().refreshProxysFromServer();//初始化本地IP代理池
    }
}
