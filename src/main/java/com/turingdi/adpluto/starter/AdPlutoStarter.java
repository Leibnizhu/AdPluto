package com.turingdi.adpluto.starter;

import java.util.Random;

import com.turingdi.adpluto.entity.GlobalProperties;
import com.turingdi.adpluto.entity.GlobalProperties.Size;
import com.turingdi.adpluto.service.DatabaseAccessor;
import com.turingdi.adpluto.service.URLAccessor;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;
import com.turingdi.adpluto.utils.MySQLUtils;

public class AdPlutoStarter {
    public static void main(String[] args) {
        Log4jUtils.getLogger().info("已读取配置文件config.json：" + GlobalProperties.getGlobalProps());//初始化基本配置
        MySQLUtils.initMySQLPool();//初始化MySQL连接池
        new AdPlutoStarter().startCheater();//启动作弊器
        MySQLUtils.closePool();//关闭MySQL连接池
    }

    private void startCheater() {
        GlobalProperties props = GlobalProperties.getGlobalProps();
        int clkCount = 0; //统计当前执行了多少次广告主落地页的PV
        int totalPV = (int) (props.getBasic().getTotaluv() * props.getBasic().getAdvPVAdvUV());
        Random rand = new Random(System.currentTimeMillis());
        while (clkCount < totalPV) { //需要触发的点击次数
            //遍历所有可能的宏替换排列组合
            for (String adxId : props.getAdxid()) {
                for (Size size : props.getSize()) {
                    for (String crtvPkgId : props.getCtid()) {
                        for (String adzoneId : props.getSpotid()) {
                            for (String tag : props.getTag()) {
                                for (String clickURL : props.getUrl()) {
                                    //URL宏替换
                                    clickURL = CommonUtils.microReplace(clickURL, adxId, size, crtvPkgId, adzoneId, tag);
                                    Log4jUtils.getLogger().debug(clickURL);
                                    //构造Cookie，访问URL
                                    URLAccessor.getInstance().accessURL(clickURL, rand);
                                    //写入扒数平台的MySQL
                                    DatabaseAccessor.getInstance().incrDataBase();
                                    clkCount++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
