package com.turingdi.adpluto.service;

import java.util.Random;

import com.turingdi.adpluto.entity.GlobalProperties;

public class URLAccessor {
	
	private static URLAccessor INSTANCE = new URLAccessor();
	
	private URLAccessor(){
		
	}
			
	public static URLAccessor getInstance(){
		return INSTANCE;
	}

	public void accessURL(String clickURL, Random rand) {
		if(rand.nextFloat() > 1/GlobalProperties.getGlobalProps().getBasic().getAdvertiserpvuv()){
			//获取一个旧的Cookie进行访问
		}
		
	}
}
