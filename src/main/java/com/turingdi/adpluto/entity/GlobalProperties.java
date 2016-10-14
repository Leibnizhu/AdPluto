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
	private String[] creativepkgid;// 创意包ID
	private String[] channelid;// 渠道ID
	private String[] adzoneid; // 广告位ID
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
		private float advertiserpvuv;// 广告主监测到的PV和UV的比值
		private float dsppvclick;// DSP端增加的PV和点击数的比值（注，DSP端监测到的点击相当于广告主网站监测到的PV）

		@Override
		public String toString() {
			return "Basic [totaluv=" + totaluv + ", advertiserpvuv=" + advertiserpvuv + ", dsppvclick=" + dsppvclick + "]";
		}

		/**
		 * @return the totaluv
		 */
		public int getTotaluv() {
			return totaluv;
		}

		/**
		 * @param totaluv
		 *            the totaluv to set
		 */
		public Basic setTotaluv(int totaluv) {
			this.totaluv = totaluv;
			return this;
		}

		/**
		 * @return the advertiserpvuv
		 */
		public float getAdvertiserpvuv() {
			return advertiserpvuv;
		}

		/**
		 * @param advertiserpvuv
		 *            the advertiserpvuv to set
		 */
		public Basic setAdvertiserpvuv(float advertiserpvuv) {
			this.advertiserpvuv = advertiserpvuv;
			return this;
		}

		/**
		 * @return the dsppvclick
		 */
		public float getDsppvclick() {
			return dsppvclick;
		}

		/**
		 * @param dsppvclick
		 *            the dsppvclick to set
		 */
		public Basic setDsppvclick(float dsppvclick) {
			this.dsppvclick = dsppvclick;
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

		public String getJDBCDriver() {
			return JDBCDriver;
		}

		public void setJDBCDriver(String JDBCDriver) {
			this.JDBCDriver = JDBCDriver;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public int getInitConns() {
			return initConns;
		}

		public void setInitConns(int initConns) {
			this.initConns = initConns;
		}

		public int getMaxConns() {
			return maxConns;
		}

		public void setMaxConns(int maxConns) {
			this.maxConns = maxConns;
		}
	}

	@Override
	public String toString() {
		return "GlobalProperties [basic=" + basic + ", url=" + Arrays.toString(url) + ", size=" + Arrays
				.toString(size) + ", creativepkgid=" + Arrays.toString(creativepkgid) + ", channelid=" + Arrays
						.toString(channelid) + ", adzoneid=" + Arrays.toString(adzoneid) + "]";
	}

	public Mysql getMysql() {
		return mysql;
	}

	public void setMysql(Mysql mysql) {
		this.mysql = mysql;
	}

	/**
	 * @return the url
	 */
	public String[] getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public GlobalProperties setUrl(String[] url) {
		this.url = url;
		return this;
	}

	/**
	 * @return the basic
	 */
	public Basic getBasic() {
		return basic;
	}

	/**
	 * @param basic
	 *            the basic to set
	 */
	public GlobalProperties setBasic(Basic basic) {
		this.basic = basic;
		return this;
	}

	/**
	 * @return the size
	 */
	public Size[] getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public GlobalProperties setSize(Size[] size) {
		this.size = size;
		return this;
	}

	/**
	 * @return the creativepkgid
	 */
	public String[] getCreativepkgid() {
		return creativepkgid;
	}

	/**
	 * @param creativepkgid
	 *            the creativepkgid to set
	 */
	public GlobalProperties setCreativepkgid(String[] creativepkgid) {
		this.creativepkgid = creativepkgid;
		return this;
	}

	/**
	 * @return the channelid
	 */
	public String[] getChannelid() {
		return channelid;
	}

	/**
	 * @param channelid
	 *            the channelid to set
	 */
	public GlobalProperties setChannelid(String[] channelid) {
		this.channelid = channelid;
		return this;
	}

	/**
	 * @return the adzoneid
	 */
	public String[] getAdzoneid() {
		return adzoneid;
	}

	/**
	 * @param adzoneid
	 *            the adzoneid to set
	 */
	public GlobalProperties setAdzoneid(String[] adzoneid) {
		this.adzoneid = adzoneid;
		return this;
	}
}
