package com.turingdi.adpluto.entity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.turingdi.adpluto.utils.CommonUtils;
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
    private Campaign[] camp;// 渠道ID
    private String[] ctid;// 创意包ID
    private String[] spotid; // 广告位ID
    private String[] tag;// 人群标签
    private Mysql mysql;//MySQL配置

    private static GlobalProperties globalProps;

    static {
        InputStream is = null;
        try {
            is = new BufferedInputStream(GlobalProperties.class.getResourceAsStream("/config.json"));
            globalProps = JSON.parseObject(is, GlobalProperties.class);
        } catch (IOException e) {
            Log4jUtils.getLogger().error("读取config.json配置文件时抛出IO异常", e);
        } finally {
            if (null != is) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打乱所有数组，以免刷多次少量的时候，访问的参数值不均匀
     */
    public void shuffle(){
        //打乱URL
        this.setUrl(CommonUtils.shuffleArray(this.getUrl()));
        //打乱size
        this.setSize(CommonUtils.shuffleArray(this.getSize()));
        //打乱camp
        this.setCamp(CommonUtils.shuffleArray(this.getCamp()));
        //打乱ctid
        this.setCtid(CommonUtils.shuffleArray(this.getCtid()));
        //打乱spotid
        this.setSpotid(CommonUtils.shuffleArray(this.getSpotid()));
        //打乱tag
        this.setTag(CommonUtils.shuffleArray(this.getTag()));

        Log4jUtils.getLogger().info("打乱后的配置对象为:" + this.toString());
    }

    /**
     * @return the globalprop
     */
    public static GlobalProperties getGlobalProps() {
        return globalProps;
    }

    public static class Basic {
        private int totaluv;// 需要投放的总UV数（广告主监测到的）
        private double advPVAdvUV;// Adobe监测到的PV和UV的比值
        private double dspClickAdvUV;//DSP端增加的点击与Adobe监测的UV的比值
        private double dspImpAdvUV; //DSP端增加的曝光量和Adobe监测UV的比值

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

        @SuppressWarnings("unused")
        public Basic setTotaluv(int totaluv) {
            this.totaluv = totaluv;
            return this;
        }

        public double getAdvPVAdvUV() {
            return advPVAdvUV;
        }

        @SuppressWarnings("unused")
        public Basic setAdvPVAdvUV(double advPVAdvUV) {
            this.advPVAdvUV = advPVAdvUV;
            return this;
        }

        public double getDspClickAdvUV() {
            return dspClickAdvUV;
        }

        @SuppressWarnings("unused")
        public Basic setDspClickAdvUV(double dspClickAdvUV) {
            this.dspClickAdvUV = dspClickAdvUV;
            return this;
        }

        public double getDspImpAdvUV() {
            return dspImpAdvUV;
        }

        @SuppressWarnings("unused")
        public Basic setDspImpAdvUV(double dspImpAdvUV) {
            this.dspImpAdvUV = dspImpAdvUV;
            return this;
        }
    }

    public static class Size {
        private String width; // 宽
        private String height;// 高

        @Override
        public String toString() {
            return width + "x" + height;
        }

        /**
         * @return the width
         */
        public String getWidth() {
            return width;
        }

        /**
         * @param width the width to set
         */
        @SuppressWarnings("unused")
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
         * @param height the height to set
         */
        @SuppressWarnings("unused")
        public Size setHeight(String height) {
            this.height = height;
            return this;
        }
    }

    public static class Campaign {
        private String adxid;//渠道ID
        private String[] campid;//活动ID

        @Override
        public String toString() {
            return "Campaign{" +
                    "adxid='" + adxid + '\'' +
                    ", campid=" + Arrays.toString(campid) +
                    '}';
        }

        public String getAdxid() {
            return adxid;
        }

        @SuppressWarnings("unused")
        public Campaign setAdxid(String adxid) {
            this.adxid = adxid;
            return this;
        }

        public String[] getCampid() {
            return campid;
        }

        @SuppressWarnings("unused")
        public Campaign setCampid(String[] campid) {
            this.campid = campid;
            return this;
        }
    }

    public static class Mysql {
        private String JDBCDriver; //驱动类名
        private String url;//MySQL数据库地址
        private String userId;//用户名
        private String password;//密码
        private int initConns;//连接池初始化连接数
        private int maxConns;//连接池最大连接数
        private String proxyApi;//查询代理列表的API
        private Map<String, String> adxidMapping;//DSP业务平台中的ADX ID和扒数平台中ADX ID的映射关系

        @Override
        public String toString() {
            return "Mysql{" +
                    "JDBCDriver='" + JDBCDriver + '\'' +
                    ", url='" + url + '\'' +
                    ", userId='" + userId + '\'' +
                    ", password='" + password + '\'' +
                    ", initConns=" + initConns +
                    ", maxConns=" + maxConns +
                    ", proxyApi='" + proxyApi + '\'' +
                    ", adxidMapping=" + adxidMapping +
                    '}';
        }

        public String getProxyApi() {
            return proxyApi;
        }

        public void setProxyApi(String proxyApi) {
            this.proxyApi = proxyApi;
        }

        public Map<String, String> getAdxidMapping() {
            return adxidMapping;
        }

        @SuppressWarnings("unused")
        public Mysql setAdxidMapping(Map<String, String> adxidMapping) {
            this.adxidMapping = adxidMapping;
            return this;
        }

        public String getJDBCDriver() {
            return JDBCDriver;
        }

        @SuppressWarnings("unused")
        public Mysql setJDBCDriver(String JDBCDriver) {
            this.JDBCDriver = JDBCDriver;
            return this;
        }

        public String getUrl() {
            return url;
        }

        @SuppressWarnings("unused")
        public Mysql setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getUserId() {
            return userId;
        }

        @SuppressWarnings("unused")
        public Mysql setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public String getPassword() {
            return password;
        }

        @SuppressWarnings("unused")
        public Mysql setPassword(String password) {
            this.password = password;
            return this;
        }

        public int getInitConns() {
            return initConns;
        }

        @SuppressWarnings("unused")
        public Mysql setInitConns(int initConns) {
            this.initConns = initConns;
            return this;
        }

        public int getMaxConns() {
            return maxConns;
        }

        @SuppressWarnings("unused")
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
                ", camp=" + Arrays.toString(camp) +
                ", ctid=" + Arrays.toString(ctid) +
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

    public Campaign[] getCamp() {
        return camp;
    }

    public GlobalProperties setCamp(Campaign[] camp) {
        this.camp = camp;
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
