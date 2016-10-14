package com.turingdi.adpluto.entity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.turingdi.adpluto.utils.Log4jUtils;

/**
 * 保存用于刷流量的全局配置
 * 
 * @author leibniz 2016-10-12 15:23
 */
public class GlobalProperties {
	private Basic basic;// 基本配置
	private String[] url;//要刷的广告主落地页，包括宏
	private Size[] size;// 尺寸
	private String[] ctid;// 创意包ID
	private String[] adxid;// 渠道ID
	private String[] spotid; // 广告位ID
	private String[] tag;// 人群标签
	private Mysql mysql;//MySQL配置
	
	private static GlobalProperties globalProps;
	
	static{
		InputStream is = null;
		try{
			is = new BufferedInputStream(GlobalProperties.class.getResourceAsStream("/config.json"));
			globalProps = JSON.parseObject(is, GlobalProperties.class);
		} catch(IOException e){
			Log4jUtils.getLogger().error("读取config.json配置文件时抛出IO异常", e);
		} finally {
			if(null != is ) try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the globalprop
	 */
	public static GlobalProperties getGlobalProps() {
		return globalProps;
	}

	public static class Basic {
		private int totaluv;// 需要投放的总UV数（广告主监测到的）
		private float advPVAdvUV;// Adobe监测到的PV和UV的比值
		private float dspClickAdvUV;//DSP端增加的点击与Adobe监测的UV的比值
		private float dspImpAdvUV; //DSP端增加的曝光量和Adobe监测UV的比值

		@Override
		public String toString() {
			return "Basic{" +
					"totaluv=" + totaluv +
					", advPVAdvUV=" + advPVAdvUV +
					", dspClickAdvUV=" + dspClickAdvUV +
					", dspImpAdvUV=" + dspImpAdvUV +
					'}';
		}

		public int getTotaluv() {
			return totaluv;
		}

		public Basic setTotaluv(int totaluv) {
			this.totaluv = totaluv;
			return this;
		}

		public float getAdvPVAdvUV() {
			return advPVAdvUV;
		}

		public Basic setAdvPVAdvUV(float advPVAdvUV) {
			this.advPVAdvUV = advPVAdvUV;
			return this;
		}

		public float getDspClickAdvUV() {
			return dspClickAdvUV;
		}

		public Basic setDspClickAdvUV(float dspClickAdvUV) {
			this.dspClickAdvUV = dspClickAdvUV;
			return this;
		}

		public float getDspImpAdvUV() {
			return dspImpAdvUV;
		}

		public Basic setDspImpAdvUV(float dspImpAdvUV) {
			this.dspImpAdvUV = dspImpAdvUV;
			return this;
		}
	}

	public static class Size {
		private String width; // 宽
		private String height;// 高

		@Override
		public String toString() {
			return "Size [width=" + width + ", height=" + height + "]";
		}

		/**
		 * @return the width
		 */
		public String getWidth() {
			return width;
		}

		/**
		 * @param width
		 *            the width to set
		 */
		public Size setWidth(String width) {
			this.width = width;
			return this;
		}

		/**
		 * @return the height
		 */
		public String getHeight() {
			return height;
		}

		/**
		 * @param height
		 *            the height to set
		 */
		public Size setHeight(String height) {
			this.height = height;
			return this;
		}
	}

	public static class Mysql{
		private String JDBCDriver; //驱动类名
		private String url;//MySQL数据库地址
		private String userId;//用户名
		private String password;//密码
		private int initConns;//连接池初始化连接数
		private int maxConns;//连接池最大连接数

		@Override
		public String toString() {
			return "Mysql{" +
					"JDBCDriver='" + JDBCDriver + '\'' +
					", url='" + url + '\'' +
					", userId='" + userId + '\'' +
					", password='" + password + '\'' +
					", initConns=" + initConns +
					", maxConns=" + maxConns +
					'}';
		}

		public String getJDBCDriver() {
			return JDBCDriver;
		}

		public Mysql setJDBCDriver(String JDBCDriver) {
			this.JDBCDriver = JDBCDriver;
			return this;
		}

		public String getUrl() {
			return url;
		}

		public Mysql setUrl(String url) {
			this.url = url;
			return this;
		}

		public String getUserId() {
			return userId;
		}

		public Mysql setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public String getPassword() {
			return password;
		}

		public Mysql setPassword(String password) {
			this.password = password;
			return this;
		}

		public int getInitConns() {
			return initConns;
		}

		public Mysql setInitConns(int initConns) {
			this.initConns = initConns;
			return this;
		}

		public int getMaxConns() {
			return maxConns;
		}

		public Mysql setMaxConns(int maxConns) {
			this.maxConns = maxConns;
			return this;
		}
	}

	@Override
	public String toString() {
		return "GlobalProperties{" +
				"basic=" + basic +
				", url=" + Arrays.toString(url) +
				", size=" + Arrays.toString(size) +
				", ctid=" + Arrays.toString(ctid) +
				", adxid=" + Arrays.toString(adxid) +
				", spotid=" + Arrays.toString(spotid) +
				", tag=" + Arrays.toString(tag) +
				", mysql=" + mysql +
				'}';
	}

	public Basic getBasic() {
		return basic;
	}

	public GlobalProperties setBasic(Basic basic) {
		this.basic = basic;
		return this;
	}

	public String[] getUrl() {
		return url;
	}

	public GlobalProperties setUrl(String[] url) {
		this.url = url;
		return this;
	}

	public Size[] getSize() {
		return size;
	}

	public GlobalProperties setSize(Size[] size) {
		this.size = size;
		return this;
	}

	public String[] getCtid() {
		return ctid;
	}

	public GlobalProperties setCtid(String[] ctid) {
		this.ctid = ctid;
		return this;
	}

	public String[] getAdxid() {
		return adxid;
	}

	public GlobalProperties setAdxid(String[] adxid) {
		this.adxid = adxid;
		return this;
	}

	public String[] getSpotid() {
		return spotid;
	}

	public GlobalProperties setSpotid(String[] spotid) {
		this.spotid = spotid;
		return this;
	}

	public String[] getTag() {
		return tag;
	}

	public GlobalProperties setTag(String[] tag) {
		this.tag = tag;
		return this;
	}

	public Mysql getMysql() {
		return mysql;
	}

	public GlobalProperties setMysql(Mysql mysql) {
		this.mysql = mysql;
		return this;
	}
}
