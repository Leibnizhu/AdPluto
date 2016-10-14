package com.turingdi.adpluto.utils;

import com.turingdi.adpluto.entity.GlobalProperties.Size;

public class CommonUtils {

	/**
	 * 以下是各种宏替换字段
	 */
	/**
	 * 宏替换，渠道id，例如是Tanx还是百度
	 */
	public static final String ADX_ID = "{adxid}";
	/**
	 * 宏替换，广告投放活动id
	 */
	public static final String CAMP_ID = "{campaignid}";
	/**
	 * 宏替换，广告位id
	 */
	public static final String ADZONE_ID = "{spotid}";
	/**
	 * 宏替换，广告位宽度
	 */
	public static final String WIDTH = "{width}";
	/**
	 * 宏替换，广告位高度
	 */
	public static final String HEIGHT = "{height}";
	/**
	 * 宏替换，创意ID
	 */
	public static final String CT_ID = "{ctid}";
	/**
	 * 宏替换，来源域名ID
	 */
	public static final String REF_DOMAIN_ID = "{refh}";
	/**
	 * 宏替换，人群标签
	 */
	public static final String TAG_ID = "{tag}";

	public static String microReplace(String url, String adxId, Size size, String crtvPkgId, String adzoneId, String tag) {
		StringBuffer sb = new StringBuffer(url);
		CommonUtils.replaceStringBuffer(sb, ADX_ID, adxId);
		//CommonUtils.replaceStringBuffer(sb, CAMP_ID, campid);
		CommonUtils.replaceStringBuffer(sb, ADZONE_ID, adzoneId);
		CommonUtils.replaceStringBuffer(sb, WIDTH, size.getWidth());
		CommonUtils.replaceStringBuffer(sb, HEIGHT, size.getHeight());
		CommonUtils.replaceStringBuffer(sb, CT_ID, crtvPkgId);
		CommonUtils.replaceStringBuffer(sb, TAG_ID, tag);
		//CommonUtils.replaceStringBuffer(sb, REF_DOMAIN_ID, refsizeid));
		return sb.toString();
	}
	
	/**
	 * 基于StringBuffer的字符串替代方法，如果StringBuffer中没有目标关键词，则不替换，跳过
	 * 
	 * @param sb
	 *            需要被替换的StringBuffer
	 * @param word
	 *            需要被替代的目标关键词
	 * @param replacement
	 *            关键词需要替代成的字符串
	 */
	public static void replaceStringBuffer(StringBuffer sb, String word, String replacement) {
		int startIndex = sb.indexOf(word);
		if (-1 != startIndex) {
			sb.replace(startIndex, startIndex + word.length(), replacement==null?"":replacement);
		}
	}

}
