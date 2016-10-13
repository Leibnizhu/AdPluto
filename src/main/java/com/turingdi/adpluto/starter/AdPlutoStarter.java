package com.turingdi.adpluto.starter;

import java.util.Random;

import com.turingdi.adpluto.entity.GlobalProperties;
import com.turingdi.adpluto.entity.GlobalProperties.Size;
import com.turingdi.adpluto.service.DatabaseAccessor;
import com.turingdi.adpluto.service.URLAccessor;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;

public class AdPlutoStarter {
	public static void main(String[] args){
		Log4jUtils.getLogger().info("已读取配置文件config.json：" + GlobalProperties.getGlobalProps());
		new  AdPlutoStarter().startCheater();
	}

	private void startCheater() {
		GlobalProperties props = GlobalProperties.getGlobalProps();
		int clkCount = 0; //统计当前执行了多少次广告主落地页的PV
		int totalPV = (int) (props.getBasic().getTotaluv()*props.getBasic().getAdvertiserpvuv());
		Random rand = new Random(System.currentTimeMillis());
		while(clkCount< totalPV){ //需要触发的点击次数
			//遍历所有可能的宏替换排列组合
			 for(String adxId : props.getChannelid()){
				 for(Size size : props.getSize()){
					 for(String crtvPkgId : props.getCreativepkgid()){
						 for(String adzoneId : props.getAdzoneid()){
							 for(String clickURL : props.getUrl()){
								//URL宏替换
								 clickURL = CommonUtils.microReplace(clickURL, adxId, size, crtvPkgId, adzoneId); 
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
