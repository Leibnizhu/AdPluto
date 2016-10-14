package com.turingdi.adpluto.entity;

/**
 * 对应
 *
 * Created by leibniz on 16-10-14.
 */
public class RequestParams {
    private String clickURL;
    private String adxId;
    private String mysqlAdxId;// 在MySQL中的ADX ID
    private GlobalProperties.Size size;
    private String crtvPkgId;
    private String campid;
    private String adzoneId;
    private String tag;

    public RequestParams(String clickURL, String adxId, GlobalProperties.Size size, String crtvPkgId, String campid, String adzoneId, String tag) {
        this.clickURL = clickURL;
        this.adxId = adxId;
        this.mysqlAdxId = GlobalProperties.getGlobalProps().getMysql().getAdxidMapping().get(adxId);
        this.size = size;
        this.crtvPkgId = crtvPkgId;
        this.campid = campid;
        this.adzoneId = adzoneId;
        this.tag = tag;
    }

    public String getMysqlAdxId() {
        return mysqlAdxId;
    }

    public void setMysqlAdxId(String mysqlAdxId) {
        this.mysqlAdxId = mysqlAdxId;
    }

    public String getCampid() {
        return campid;
    }

    public String getClickURL() {
        return clickURL;
    }

    public String getAdxId() {
        return adxId;
    }

    public GlobalProperties.Size getSize() {
        return size;
    }

    public String getCrtvPkgId() {
        return crtvPkgId;
    }

    public String getAdzoneId() {
        return adzoneId;
    }

    public String getTag() {
        return tag;
    }
}
