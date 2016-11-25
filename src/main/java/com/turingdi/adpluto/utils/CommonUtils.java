package com.turingdi.adpluto.utils;

import com.turingdi.adpluto.entity.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CommonUtils {
    /**
     * 宏替换，渠道id，例如是Tanx还是百度
     */
    private static final String ADX_ID = "{adxid}";
    /**
     * 宏替换，广告投放活动id
     */
    private static final String CAMP_ID = "{campaignid}";
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
     * 宏替换，人群标签
     */
    private static final String TAG_ID = "{tag}";

    public static void microReplace(RequestParams req) {
        StringBuffer sb = new StringBuffer(req.getClickURL());
        CommonUtils.replaceStringBuffer(sb, ADX_ID, req.getAdxId());
        CommonUtils.replaceStringBuffer(sb, CAMP_ID, req.getCampid());
        CommonUtils.replaceStringBuffer(sb, ADZONE_ID, req.getAdzoneId());
        CommonUtils.replaceStringBuffer(sb, WIDTH, req.getSize().getWidth());
        CommonUtils.replaceStringBuffer(sb, HEIGHT, req.getSize().getHeight());
        CommonUtils.replaceStringBuffer(sb, CT_ID, req.getCrtvPkgId());
        CommonUtils.replaceStringBuffer(sb, TAG_ID, req.getTag());
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

    @SuppressWarnings("unchecked")
    public static <T> T[] shuffleArray(T[] source){
        List<T> list = Arrays.asList(source);
        Collections.shuffle(list);
        return (T[])list.toArray();
    }

    public static String getRandomIPAddr(){
        Random rand = new Random(System.currentTimeMillis());
        return new StringBuffer()
                .append(rand.nextInt(100)+150)
                .append(".")
                .append(rand.nextInt(100)+150)
                .append(".")
                .append(rand.nextInt(100)+150)
                .append(".")
                .append(rand.nextInt(100)+150)
                .toString();
    }

    public static String sendGetRequest(String url){
        BufferedReader in = null;
        StringBuffer sbuf = new StringBuffer();
        try {
            URL reqURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) reqURL.openConnection(); // 进行连接，但是实际上getrequest要在下一句的connection.getInputStream() 函数中才会真正发到服务器
            connection.setDoOutput(false);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(200);
            connection.setDoInput(true);
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
                sbuf.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sbuf.toString();
    }
}
