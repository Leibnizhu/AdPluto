package com.turingdi.adpluto.utils;

import com.turingdi.adpluto.entity.RequestParams;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonUtils {

    /**
     * 以下是各种宏替换字段
     */
    /**
     * 宏替换，渠道id，例如是Tanx还是百度
     */
    private static final String ADX_ID = "{adxid}";
    /**
     * 宏替换，广告投放活动id
     */
//    private static final String CAMP_ID = "{campaignid}";
    /**
     * 宏替换，广告位id
     */
    private static final String ADZONE_ID = "{spotid}";
    /**
     * 宏替换，广告位宽度
     */
    private static final String WIDTH = "{width}";
    /**
     * 宏替换，广告位高度
     */
    private static final String HEIGHT = "{height}";
    /**
     * 宏替换，创意ID
     */
    private static final String CT_ID = "{ctid}";
    /**
     * 宏替换，来源域名ID
     */
//    private static final String REF_DOMAIN_ID = "{refh}";
    /**
     * 宏替换，人群标签
     */
    private static final String TAG_ID = "{tag}";

    public static void microReplace(RequestParams req) {
        StringBuffer sb = new StringBuffer(req.getClickURL());
        CommonUtils.replaceStringBuffer(sb, ADX_ID, req.getAdxId());
        //CommonUtils.replaceStringBuffer(sb, CAMP_ID, campid);
        CommonUtils.replaceStringBuffer(sb, ADZONE_ID, req.getAdzoneId());
        CommonUtils.replaceStringBuffer(sb, WIDTH, req.getSize().getWidth());
        CommonUtils.replaceStringBuffer(sb, HEIGHT, req.getSize().getHeight());
        CommonUtils.replaceStringBuffer(sb, CT_ID, req.getCrtvPkgId());
        CommonUtils.replaceStringBuffer(sb, TAG_ID, req.getTag());
        //CommonUtils.replaceStringBuffer(sb, REF_DOMAIN_ID, refsizeid));
        req.setClickURL(sb.toString());
    }

    /**
     * 基于StringBuffer的字符串替代方法，如果StringBuffer中没有目标关键词，则不替换，跳过
     *
     * @param sb          需要被替换的StringBuffer
     * @param word        需要被替代的目标关键词
     * @param replacement 关键词需要替代成的字符串
     */
    private static void replaceStringBuffer(StringBuffer sb, String word, String replacement) {
        int startIndex = sb.indexOf(word);
        if (-1 != startIndex) {
            sb.replace(startIndex, startIndex + word.length(), replacement == null ? "" : replacement);
        }
    }

    public static <T> T[] shuffleArray(T[] source){
        List<T> list = Arrays.asList(source);
        Collections.shuffle(list);
        return (T[])list.toArray();
    }
}
