package com.turingdi.adpluto.starter;

import com.turingdi.adpluto.entity.GlobalProperties;
import com.turingdi.adpluto.entity.GlobalProperties.Size;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;

public class AdPlutoStarter {
	public static void main(String[] args){
		Log4jUtils.getLogger().info("已读取配置文件config.json：" + GlobalProperties.getGlobalProps());
		new  AdPlutoStarter().startCheater();
	}

	private void startCheater() {
		GlobalProperties props = GlobalProperties.getGlobalProps();
		int clkCount = 0; 
		while(clkCount< props.getBasic().getTotaluv()*props.getBasic().getAdvertiserpvuv()){ //需要触发的点击次数
			 for(String adxId : props.getChannelid()){
				 for(Size size : props.getSize()){
					 for(String crtvPkgId : props.getCreativepkgid()){
						 for(String adzoneId : props.getAdzoneid()){
							 for(String clickURL : props.getUrl()){
								//URL宏替换
								 clickURL = CommonUtils.microReplace(clickURL, adxId, size, crtvPkgId, adzoneId); 
								 Log4jUtils.getLogger().debug(clickURL);
								 //构造Cookie，访问URL
								 accessURL(clickURL);
								 //写入扒数平台的MySQL
								 
								 clkCount++;
							 }
						 }
					 }
				 }
			 }
		 }
	}

	private void accessURL(String clickURL) {
		// TODO Auto-generated method stub
		
	}
}
