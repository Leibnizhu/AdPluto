package com.turingdi.adpluto.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.turingdi.adpluto.entity.GlobalProperties;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLUtils {
    private static ComboPooledDataSource cpds;

    public static void initMySQLPool() {
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(GlobalProperties.getGlobalProps().getMysql().getJDBCDriver());
            cpds.setJdbcUrl(GlobalProperties.getGlobalProps().getMysql().getUrl());
            cpds.setUser(GlobalProperties.getGlobalProps().getMysql().getUserId());
            cpds.setPassword(GlobalProperties.getGlobalProps().getMysql().getPassword());
            cpds.setInitialPoolSize(GlobalProperties.getGlobalProps().getMysql().getInitConns());
            cpds.setMaxPoolSize(GlobalProperties.getGlobalProps().getMysql().getMaxConns());
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


    public static void closePool() {
        if(null != cpds){
            cpds.close();
        }
    }
}
