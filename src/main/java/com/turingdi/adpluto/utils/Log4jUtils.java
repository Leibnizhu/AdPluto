package com.turingdi.adpluto.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Log4j 相关的工具类
 *
 * @author leibniz
 */
public class Log4jUtils {
    private final static Logger logger = LogManager.getLogger(Log4jUtils.class);

    /**
     * 获取Logger静态变量
     *
     * @return Logger静态变量
     */
    public static Logger getLogger() {
        return logger;
    }
}
