package com.turingdi.adpluto.test;

import com.turingdi.adpluto.cheat.MissionManager;
import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.MissionResponse;
import org.junit.Test;

/*
 * Created by leibniz on 16-11-30.
 */
public class EntryTest {
    @Test
    public void testImportWay() throws InterruptedException {
        MissionConfig config= MissionConfig.parseFromDefaultFile();
        MissionManager.initialize();
        //新增任务
        MissionConfig.Mission add = new MissionConfig.Mission();
        add.setId("1");
        add.setType(1);
        config.setMission(add);
        MissionResponse resp = MissionManager.getInstance().addOneMission(config);
        System.out.println(resp);
        //查询任务
        MissionConfig.Mission check = new MissionConfig.Mission();
        check.setId(resp.getCheaterID());
        check.setType(2);
        config.setMission(check);
        System.out.println(MissionManager.getInstance().addOneMission(config));
        Thread.sleep(1000*120);
        //关闭任务
        MissionConfig.Mission shutdown = new MissionConfig.Mission();
        shutdown.setId(resp.getCheaterID());
        shutdown.setType(3);
        config.setMission(shutdown);
        System.out.println(MissionManager.getInstance().addOneMission(config));
        //再查询
        config.setMission(check);
        System.out.println(MissionManager.getInstance().addOneMission(config));
        Thread.sleep(1000*30);
    }
}
