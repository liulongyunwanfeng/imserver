package com.eplat.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormat {
	/**
	 * 整数的格式化
	 * 
	 * @param value
	 * @param formatString
	 * @return
	 * @throws Exception
	 */
	public static String format(int value, String formatString)
			throws Exception {
		try {
			DecimalFormat format = new DecimalFormat(formatString);
			return format.format(value);
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 常整数的格式化
	 * 
	 * @param value
	 * @param formatString
	 * @return
	 * @throws Exception
	 */
	public static String format(long value, String formatString)
			throws Exception {
		try {
			DecimalFormat format = new DecimalFormat(formatString);
			return format.format(value);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 浮点数的格式化
	 * 
	 * @param value
	 * @param formatString
	 * @return
	 * @throws Exception
	 */
	public static String format(double value, String formatString)
			throws Exception {
		try {
			DecimalFormat format = new DecimalFormat(formatString);
			return format.format(value);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 浮点数的格式化
	 * 
	 * @param value
	 * @param formatString
	 * @return
	 * @throws Exception
	 */
	public static String format(float value, String formatString)
			throws Exception {
		try {
			DecimalFormat format = new DecimalFormat(formatString);
			return format.format(value);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 格式化整数
	 * 
	 * @param value
	 * @param formatString
	 * @return
	 * @throws Exception
	 */
	public static String format(Integer value, String formatString)
			throws Exception {
		try {
			if (value == null) {
				value = new Integer(0);
			}
			DecimalFormat format = new DecimalFormat(formatString);
			return format.format(value);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param value
	 * @param formatString
	 * @return
	 * @throws Exception
	 */
	public static String format(Float value, String formatString)
			throws Exception {
		try {
			if (value == null) {
				value = new Float(0);
			}
			DecimalFormat format = new DecimalFormat(formatString);
			return format.format(value);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public static String format(BigDecimal value, String formatString)
			throws Exception {
		try {
			if (value == null) {
				value = new java.math.BigDecimal(0);
			}
			DecimalFormat format = new DecimalFormat(formatString);
			return format.format(value);

		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 日期类型的格式化
	 * 
	 * @param value
	 * @param formatString
	 * @return
	 * @throws Exception
	 */
	public static String format(Date value, String formatString)
			throws Exception {
		try {
			if (value == null) {
				return "";
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat(formatString);
				return sdf.format(value);
			}
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			java.math.BigDecimal decimal = null;
			System.out.print(DataFormat.format(decimal, "#,###.0"));
		} catch (Exception e) {			
			e.printStackTrace();
		}

	}

}