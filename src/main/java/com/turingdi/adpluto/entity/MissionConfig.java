package com.turingdi.adpluto.entity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.turingdi.adpluto.utils.CommonUtils;
import com.turingdi.adpluto.utils.Log4jUtils;

/**
 * 保存用于刷流量的全局配置
 *
 * @author leibniz 2016-10-12 15:23
 */
@SuppressWarnings("unused")
public class MissionConfig {
    private Basic basic;// 基本配置
    private String[] url;//要刷的广告主落地页，包括宏
    private Size[] size;// 尺寸
    private Campaign[] camp;// 渠道ID
    private String[] ctid;// 创意包ID
    private String[] spotid; // 广告位ID
    private String[] tag;// 人群标签

    public MissionConfig(){}


    public static MissionConfig parseFrom(String json){
        MissionConfig result = JSON.parseObject(json, MissionConfig.class);
        result.calPvRatio();
        return result;
    }

    public static MissionConfig parseFrom(byte[] json){
        MissionConfig result = JSON.parseObject(json, MissionConfig.class);
        result.calPvRatio();
        return result;
    }

    private static MissionConfig parseFrom(InputStream in) throws IOException {
        MissionConfig result = JSON.parseObject(in, MissionConfig.class);
        result.calPvRatio();
        return result;
    }

    private void calPvRatio() {
        Basic basic = this.getBasic();
        basic.setPvClick(basic.getDspClickAdvUV() / basic.getAdvPVAdvUV());
        basic.setPvImpl(basic.getDspImpAdvUV() / basic.getAdvPVAdvUV());
    }

    public static MissionConfig parseFromDefaultFile() {
        InputStream is = null;
        try {
            is = new BufferedInputStream(MissionConfig.class.getResourceAsStream("/config.json"));
            MissionConfig config = MissionConfig.parseFrom(is);
            config.shuffle();//打乱配置
            return config;
        } catch (IOException e) {
            Log4jUtils.getLogger().error("读取config.json配置文件时抛出IO异常", e);
        } finally {
            if (null != is) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 打乱所有数组，以免刷多次少量的时候，访问的参数值不均匀
     */
    private void shuffle(){
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
    }

    public static class Basic {
        private int totaluv;// 需要投放的总UV数（广告主监测到的）
        private double advPVAdvUV;// Adobe监测到的PV和UV的比值
        private double dspClickAdvUV;//DSP端增加的点击与Adobe监测的UV的比值
        private double dspImpAdvUV; //DSP端增加的曝光量和Adobe监测UV的比值

        private double pvClick;
        private double pvImpl;

        @Override
        public String toString() {
            return "Basic{" +
                    "totaluv=" + totaluv +
                    ", advPVAdvUV=" + advPVAdvUV +
                    ", dspClickAdvUV=" + dspClickAdvUV +
                    ", dspImpAdvUV=" + dspImpAdvUV +
                    ", pvClick=" + pvClick +
                    ", pvImpl=" + pvImpl +
                    '}';
        }

        public double getPvClick() {
            return pvClick;
        }

        public void setPvClick(double pvClick) {
            this.pvClick = pvClick;
        }

        public double getPvImpl() {
            return pvImpl;
        }

        public void setPvImpl(double pvImpl) {
            this.pvImpl = pvImpl;
        }

        public int getTotaluv() {
            return totaluv;
        }


        public Basic setTotaluv(int totaluv) {
            this.totaluv = totaluv;
            return this;
        }

        public double getAdvPVAdvUV() {
            return advPVAdvUV;
        }


        public Basic setAdvPVAdvUV(double advPVAdvUV) {
            this.advPVAdvUV = advPVAdvUV;
            return this;
        }

        public double getDspClickAdvUV() {
            return dspClickAdvUV;
        }


        public Basic setDspClickAdvUV(double dspClickAdvUV) {
            this.dspClickAdvUV = dspClickAdvUV;
            return this;
        }

        public double getDspImpAdvUV() {
            return dspImpAdvUV;
        }


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


        public Campaign setAdxid(String adxid) {
            this.adxid = adxid;
            return this;
        }

        public String[] getCampid() {
            return campid;
        }


        public Campaign setCampid(String[] campid) {
            this.campid = campid;
            return this;
        }
    }

    @Override
    public String toString() {
        return "MissionConfig{" +
                "basic=" + basic +
                ", url=" + Arrays.toString(url) +
                ", size=" + Arrays.toString(size) +
                ", camp=" + Arrays.toString(camp) +
                ", ctid=" + Arrays.toString(ctid) +
                ", spotid=" + Arrays.toString(spotid) +
                ", tag=" + Arrays.toString(tag) +
                '}';
    }

    public Basic getBasic() {
        return basic;
    }

    public MissionConfig setBasic(Basic basic) {
        this.basic = basic;
        return this;
    }

    public String[] getUrl() {
        return url;
    }

    public MissionConfig setUrl(String[] url) {
        this.url = url;
        return this;
    }

    public Size[] getSize() {
        return size;
    }

    public MissionConfig setSize(Size[] size) {
        this.size = size;
        return this;
    }

    public String[] getCtid() {
        return ctid;
    }

    public MissionConfig setCtid(String[] ctid) {
        this.ctid = ctid;
        return this;
    }

    public Campaign[] getCamp() {
        return camp;
    }

    public MissionConfig setCamp(Campaign[] camp) {
        this.camp = camp;
        return this;
    }

    public String[] getSpotid() {
        return spotid;
    }

    public MissionConfig setSpotid(String[] spotid) {
        this.spotid = spotid;
        return this;
    }

    public String[] getTag() {
        return tag;
    }

    public MissionConfig setTag(String[] tag) {
        this.tag = tag;
        return this;
    }
}
