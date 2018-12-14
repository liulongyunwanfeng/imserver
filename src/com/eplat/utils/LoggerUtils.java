package com.eplat.utils;

import org.apache.log4j.Logger;


/**
 * 日志封装类
 * 
 * @author Administrator
 *
 */
public class LoggerUtils {
	public static Logger logger = Logger.getLogger(LoggerUtils.class);

	public static void info(String classz, String message) {
		logger.info("[" + classz + "]" + message);
	}

	public static void warn(String classz, String message) {
		logger.warn("[" + classz + "]" + message);
	}

	public static void debug(String classz, String message) {
		logger.debug("[" + classz + "]" + message);
	}

	public static void error(String classz, String message) {
		logger.error("[" + classz + "]" + message);
	}
	
}
