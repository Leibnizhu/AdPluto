package com.turingdi.adpluto.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.turingdi.adpluto.entity.SystemConfig;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLUtils {
    private static ComboPooledDataSource cpds;

    public static void initMySQLPool() {
        cpds = new ComboPooledDataSource();
        try {
            SystemConfig sysConfig = SystemConfig.getInstance();
            cpds.setDriverClass(sysConfig.getJDBCDriver());
            cpds.setJdbcUrl(sysConfig.getUrl());
            cpds.setUser(sysConfig.getUserId());
            cpds.setPassword(sysConfig.getPassword());
            cpds.setInitialPoolSize(sysConfig.getInitConns());
            cpds.setMaxPoolSize(sysConfig.getMaxConns());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = cpds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeResources(AutoCloseable... resources) {
        for(AutoCloseable resource : resources){
            closeAutoClosableResource(resource);
        }
    }

    private static void closeAutoClosableResource(AutoCloseable resource) {
        if (null != resource) try {
            resource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closePool() {
        if (null != cpds) {
            cpds.close();
        }
    }
}
