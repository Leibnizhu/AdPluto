package com.turingdi.adpluto.service;

import com.turingdi.adpluto.entity.MissionConfig;
import com.turingdi.adpluto.entity.RequestParams;
import com.turingdi.adpluto.utils.Log4jUtils;
import com.turingdi.adpluto.utils.MySQLUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DatabaseAccessor {

    private static DatabaseAccessor INSTANCE = new DatabaseAccessor();

    private DatabaseAccessor() {
    }

    public static DatabaseAccessor getInstance() {
        return INSTANCE;
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public boolean incrDataBase(RequestParams req, MissionConfig missionConfig) {
        String date = SDF.format(new Date());
        int addClick = calAddClick(missionConfig.getBasic().getPvClick());
        int addPV = calAddPV(missionConfig.getBasic().getPvImpl());
        return insertReport(req, date, addClick, addPV) & insertDetail(req, date, addClick, addPV);
    }

    private boolean insertDetail(RequestParams req, String date, int addClick, int addPV) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = MySQLUtils.getConnection();
            String sql = "INSERT INTO adpluto_rpt_detail (rpt_date, iddsp, campid, ctid, size, tag, pv, click) VALUES (DATE(?),?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE pv=pv+?, click=click+?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setString(2, req.getMysqlAdxId());
            stmt.setString(3, req.getCampid());
            stmt.setString(4, req.getCrtvPkgId());
            stmt.setString(5, req.getSize().toString());
            stmt.setString(6, req.getTag());
            stmt.setInt(7, addPV);
            stmt.setInt(8, addClick);
            stmt.setInt(9, addPV);
            stmt.setInt(10, addClick);
            Log4jUtils.getLogger().info("写入数据库adpluto_rpt_detail表");
            return stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQLUtils.closeResources(stmt, conn);
        }
        return false;
    }

    private boolean insertReport(RequestParams req, String date, int addClick, int addPV) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = MySQLUtils.getConnection();
            String sql = "INSERT INTO adpluto_rpt (rpt_date, iddsp, campid, pv, click) VALUES (DATE(?),?,?,?,?) ON DUPLICATE KEY UPDATE pv=pv+?, click=click+?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            stmt.setString(2, req.getMysqlAdxId());
            stmt.setString(3, req.getCampid());
            stmt.setInt(4, addPV);
            stmt.setInt(5, addClick);
            stmt.setInt(6, addPV);
            stmt.setInt(7, addClick);
            Log4jUtils.getLogger().info("写入数据库adpluto_rpt表");
            return stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQLUtils.closeResources(stmt, conn);
        }
        return false;
    }

    /**
     * 为当前插入MySQL计算一个Adobe上的PV对应多少个点击
     *
     * @param pvCLick Adobe上的PV对应多少个点击的比例
     * @return Adobe上的pv对应的点击数
     */
    private int calAddClick(double pvCLick) {
        return getRandomRound(pvCLick);
    }

    /**
     * 为当前插入MySQL计算一个Adobe上的PV对应多少个展示
     *
     * @param pvImpl Adobe上的PV对应多少个展示的比例
     * @return Adobe上的PV对应的展示数
     */
    private int calAddPV(double pvImpl) {
        return getRandomRound(pvImpl);
    }

    /**
     * 按照ratio的小数位的比例，随机返回ratio的向上或向下取整的int数，这些数最终的期望值为ratio。
     * 如ratio=2.5，则等比例返回2和3；ratio=2.2，则按1:4的比例返回3和2.
     * 随机按小数部分的比例决定向上取整或者向下取整.
     * dspPVClick = head + tail 整数和小数部分.
     * rand.nextDouble() > tail时向下取整，反之向上取整，
     * 即有1-tail的概率取head，tail的概率取head+1.
     * 最终期望值 E(addPV) = (1-tail)*head + tail*(head+1) = head + tail = dspPVClick.
     *
     * @param ratio 指定的取整数
     * @return 取整结果
     */
    private int getRandomRound(double ratio) {
        Random rand = new Random(System.currentTimeMillis());
        double tail = ratio - Math.floor(ratio); //小数部分
        if (rand.nextDouble() > tail) {
            return (int) Math.floor(ratio);
        } else {
            return (int) Math.ceil(ratio);
        }
    }
}
