package com.eplat.utils;

import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * @项目名称：eplat
 * @类名称：DateTimeUtils
 * @类描述：日期时间工具类，进行各种日期时间格式的转化以及格式化
 * @创建人：高洋
 * @创建时间：2010-2-20 下午02:22:36
 * @修改人：高洋
 * @修改时间：2010-2-20 下午02:22:36
 * @修改备注：
 * @version
 */
public class DateTimeUtils {
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String DATE_FORMAT_CN = "yyyy年MM月dd日";
	public final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";
	public final static String MONTH_FORMAT = "yyyy-MM";
	public final static String MONTH_FORMAT_MM = "yyyyMM";
	public final static String DATE_FORMAT_SHORT = "yyyyMMdd";
	public static float compHour(java.sql.Timestamp date1, java.sql.Timestamp date2) {
		return (float) ((float) (date1.getTime() - date2.getTime()) / 3600 / 1000.0);

	}
	public static int dayOfYear(java.sql.Timestamp saletime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date(saletime.getTime()));
		 return   cal.get(Calendar.DAY_OF_YEAR);  
	}
	/**
	 * 比较日期的相差天数
	 * 
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	public static int compDateOfDay(java.util.Date begindate, java.util.Date enddate) {
		return (int) ((begindate.getTime() - enddate.getTime()) / 3600 / 24 / 1000);

	}
	/**
	 * 
	 * getCurrentLongDate
	 * 
	 * @描述：获取当前时间字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String getCurrentLongDate() throws Exception {
		return DateTimeUtils.dateToLongStr(new java.util.Date());
	}

	public static String getCurrentDate() throws Exception {
		return DateTimeUtils.dateToStr(new java.util.Date());
	}

	/**
	 * 
	 * getSqlLongDate
	 * 
	 * @描述：获取SQL当前时间
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.sql.Timestamp getSqlLongDate() throws Exception {
		return new java.sql.Timestamp(new java.util.Date().getTime());
	}

	/**
	 * 
	 * getSqlDate
	 * 
	 * @描述：获取jdbc时间的当前时间
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.sql.Date getSqlDate() throws Exception {
		return new java.sql.Date(new java.util.Date().getTime());
	}

	/**
	 * 
	 * getCurrentLongDate_CN
	 * 
	 * @描述：获取当前时间字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String getCurrentLongDate_CN() throws Exception {
		return DateTimeUtils.dateToLongStr_CN(new java.util.Date());
	}

	/**
	 * 
	 * strToSqlDate
	 * 
	 * @描述：将界面输入的字符串日期， 转换成数据库的日期型
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.sql.Date strToSqlDate(String date) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return new java.sql.Date(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 
	 * strToSqlDate_CN
	 * 
	 * @描述：将界面输入的字符串日期， 转换成数据库的日期型
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.sql.Date strToSqlDate_CN(String date) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_CN);
			return new java.sql.Date(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}
	
	public static String strToSqlstr_CN(String date) throws Exception {
		return date.substring(0, 4)+"年"+date.substring(5, 7)+"月"+date.substring(8,10)+"日";
	}

	/**
	 * 
	 * strToDate
	 * 
	 * @描述：从字符串转换到java的时间对象
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date strToDate(String date) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return new java.util.Date(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}
	
	public static String strToDateshortStr(java.util.Date date) throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);
			return sdf.format(new java.util.Date(date.getTime()));
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	public static String strToDateshort() throws Exception {
		return DateTimeUtils.strToDateshortStr(new java.util.Date());
	}
	
	public static java.util.Date strToDateshort(String date) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);
			return new java.util.Date(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 
	 * strToDate_CN
	 * 
	 * @描述：从字符串转换到java的时间对象
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date strToDate_CN(String date) throws Exception {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_CN);
			return new java.util.Date(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 
	 * sqlDateToStr
	 * 
	 * @描述：将日期转换成短日期字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String sqlDateToStr(java.sql.Date date) throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return sdf.format(new java.util.Date(date.getTime()));
		} catch (RuntimeException e) {
			throw e;
		}

	}
	public static String sqlDateToStr(java.sql.Timestamp date) throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return sdf.format(new java.util.Date(date.getTime()));
		} catch (RuntimeException e) {
			throw e;
		}

	}
	/**
	 * 
	 * sqlDateToStr_CN
	 * 
	 * @描述：将日期转换成短日期字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String sqlDateToStr_CN(java.sql.Date date) throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_CN);
			return sdf.format(new java.util.Date(date.getTime()));
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 
	 * dateToStr
	 * 
	 * @描述：将java时间转换成字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String dateToStr(java.util.Date date) throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return sdf.format(date);
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 
	 * dateToStr_CN
	 * 
	 * @描述：将java时间转换成字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String dateToStr_CN(java.util.Date date) throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_CN);
			return sdf.format(date);
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 
	 * sqlDateToLongStr
	 * 
	 * @描述：将jdbc的日期实践性转换成字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String sqlDateToLongStr(java.sql.Timestamp date)
			throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
			return sdf.format(new java.util.Date(date.getTime()));
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 
	 * sqlDateToLongStr_CN
	 * 
	 * @描述：将jdbc的日期实践性转换成字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String sqlDateToLongStr_CN(java.sql.Timestamp date)
			throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_CN);
			return sdf.format(new java.util.Date(date.getTime()));
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 
	 * dateToLongStr
	 * 
	 * @描述：将java的日期格式化成字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String dateToLongStr(java.util.Date date) throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
			return sdf.format(date);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 
	 * dateToLongStr_CN
	 * 
	 * @描述：将java的日期格式化成字符串
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static String dateToLongStr_CN(java.util.Date date) throws Exception {
		try {
			if (date == null)
				return "";
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_CN);
			return sdf.format(date);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 
	 * strToSqlLongDate
	 * 
	 * @描述：转换为jdbc日期时间型
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.sql.Timestamp strToSqlLongDate(String date)
			throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
			return new java.sql.Timestamp(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * strToSqlLongDate
	 * 
	 * @描述：转换为jdbc日期时间型
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.sql.Timestamp strToSqlLongStamp(String date)
			throws Exception {
		try {
		  SimpleDateFormat sdf = null;
		  if (date.length()==10){
			  sdf = new SimpleDateFormat(DATE_FORMAT); 
		  }else{
			  sdf = new SimpleDateFormat(TIME_FORMAT); 
		  }
		  return new java.sql.Timestamp(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}
	
	public static java.sql.Date strToSqlLongDatepd(String date)
			throws Exception {
		try {
		  SimpleDateFormat sdf = null;
		  if (date.length()==10){
			  sdf = new SimpleDateFormat(DATE_FORMAT); 
		  }else{
			  sdf = new SimpleDateFormat(TIME_FORMAT); 
		  }
		  return new java.sql.Date(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 
	 * strToSqlLongDate_CN
	 * 
	 * @描述：转换为jdbc日期时间型
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.sql.Timestamp strToSqlLongDate_CN(String date)
			throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_CN);
			return new java.sql.Timestamp(sdf.parse(date).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 
	 * strToLongDate
	 * 
	 * @描述：将字符串转换成java的日期类型
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date strToLongDate(String date) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
			return sdf.parse(date);
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 
	 * strToLongDate_CN
	 * 
	 * @描述：将字符串转换成java的日期类型
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date strToLongDate_CN(String date) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_CN);
			return sdf.parse(date);
		} catch (ParseException e) {
			throw e;
		}
	}

	/**
	 * 
	 * getNextDate
	 * 
	 * @描述：日期的下一天
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date getNextDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day + 1);
		return calendar.getTime();
	}

	/**
	 * 
	 * incDay
	 * 
	 * @描述：加天数
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date incDay(java.util.Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day + days);
		return calendar.getTime();

	}
	public static java.util.Date incHour(java.util.Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.HOUR, day + days);
		return calendar.getTime();

	}
	/**
	 * 
	 * incMonth
	 * 
	 * @描述：加月份
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public static java.util.Date incMonth(java.util.Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month + months);

		return calendar.getTime();

	}

	/**
	 * 
	 * incYear
	 * 
	 * @描述：添加年
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date incYear(java.util.Date date, int years) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		calendar.set(Calendar.YEAR, year + years);
		return calendar.getTime();

	}

	/**
	 * 
	 * incMinute
	 * 
	 * @描述：添加分钟
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date incMinute(java.util.Date date, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int minute = calendar.get(Calendar.MINUTE);
		calendar.set(Calendar.MINUTE, minute + minutes);
		return calendar.getTime();

	}

	/**
	 * 
	 * getPreviousDate
	 * 
	 * @描述：获得某一日期的前一天
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date getPreviousDate(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - 1);
		return calendar.getTime();
	}

	/**
	 * 
	 * getFirstDayOfMonth
	 * 
	 * @描述：获得某年某月第一天的日期
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date getFirstDayOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, 1);
		return calendar.getTime();
	}

	/**
	 * 
	 * getLastDayOfMonth
	 * 
	 * @描述：获得某年某月最后一天的日期
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date getLastDayOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		return getPreviousDate(calendar.getTime());
	}

	/**
	 * 
	 * buildDate
	 * 
	 * @描述：由年月日构建
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date buildDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		return calendar.getTime();
	}

	/**
	 * 
	 * getDayCountOfMonth
	 * 
	 * @描述：取得某月的天数
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static int getDayCountOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 0);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * ]
	 * 
	 * getLastDayOfQuarter
	 * 
	 * @描述： 获得某年某季度的最后一天的日期
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date getLastDayOfQuarter(int year, int quarter) {
		int month = 0;
		if (quarter > 4) {
			return null;
		} else {
			month = quarter * 3;
		}
		return getLastDayOfMonth(year, month);

	}

	/**
	 * 
	 * getFirstDayOfQuarter
	 * 
	 * @描述：获得某年某季度的第一天的日期
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static java.util.Date getFirstDayOfQuarter(int year, int quarter) {
		int month = 0;
		if (quarter > 4) {
			return null;
		} else {
			month = (quarter - 1) * 3 + 1;
		}
		return getFirstDayOfMonth(year, month);
	}

	/**
	 * 获得某年的第一天的日期
	 * 
	 * @param year
	 * @return Date
	 */
	public static java.util.Date getFirstDayOfYear(int year) {
		return getFirstDayOfMonth(year, 1);
	}

	/**
	 * 获得某年的最后一天的日期
	 * 
	 * @param year
	 * @return Date
	 */
	public static java.util.Date getLastDayOfYear(int year) {
		return getLastDayOfMonth(year, 12);
	}

	/**
	 * 将不足两位的月份或日期补足为两位
	 * 
	 * @param decimal
	 * @return
	 */
	public static String formatMonthDay(int decimal) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(decimal);
	}

	/**
	 * 比较日期，相差整数部分是天 返回值小于0 date1早于date2 返回值大于0 date1晚于date2
	 * 
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static float compDate(Date date1, Date date2) {
		return (float) ((float) (date1.getTime() - date2.getTime()) / 3600 / 24 / 1000.0);

	}

	/**
	 * 比较日期的相差天数
	 * 
	 * @param enddate
	 * @param begindate
	 * @return
	 */
	public static int compDateOfDay(Date enddate,Date begindate) {
		return (int) ((enddate.getTime() - begindate.getTime()) / 3600 / 24 / 1000);

	}
	
	public static int compDateOfDay1(java.util.Date enddate, java.util.Date begindate) {
		return (int) ((enddate.getTime() - begindate.getTime()) / 3600 / 24 / 1000);

	}

	/**
	 * 
	 * dateDiff
	 * 
	 * @描述：计算日期相差天数
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public int dateDiff(Object beginDate, Object endDate, String flag) {
		if (!StringUtils.hasLength(flag)) {
			return -1;
		} else {
			if ((beginDate == null) || (endDate == null)) {
				return 0;
			}
			java.util.Date beginTime = null;
			java.util.Date endTime = null;
			if (beginDate instanceof java.util.Date) {
				beginTime = (java.util.Date) beginDate;
			} else if (beginDate instanceof java.sql.Date) {
				java.sql.Date sqldate = (java.sql.Date) beginDate;
				beginTime = new java.util.Date(sqldate.getTime());
			} else if (beginDate instanceof java.sql.Timestamp) {
				java.sql.Timestamp sqltime = (java.sql.Timestamp) beginDate;
				beginTime = new java.util.Date(sqltime.getTime());
			}
			if (endDate instanceof java.util.Date) {
				endTime = (java.util.Date) endDate;
			} else if (endDate instanceof java.sql.Date) {
				java.sql.Date sqldate = (java.sql.Date) endDate;
				endTime = new java.util.Date(sqldate.getTime());
			} else if (endDate instanceof java.sql.Timestamp) {
				java.sql.Timestamp sqltime = (java.sql.Timestamp) endDate;
				endTime = new java.util.Date(sqltime.getTime());
			}
			if (flag.equalsIgnoreCase("year")) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(beginTime);
				int beginYear = calendar.get(Calendar.YEAR);
				calendar.setTime(endTime);
				int endYear = calendar.get(Calendar.YEAR);
				return beginYear - endYear;
			} else if (flag.equalsIgnoreCase("month")) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(beginTime);
				int beginYear = calendar.get(Calendar.YEAR);
				int beginMonth = calendar.get(Calendar.MONTH);
				calendar.setTime(endTime);
				int endYear = calendar.get(Calendar.YEAR);
				int endMonth = calendar.get(Calendar.MONTH);
				return (beginYear * 12 + beginMonth)
						- (endYear * 12 + endMonth);
			} else if (flag.equalsIgnoreCase("day")) {
				return (int) ((beginTime.getTime() - endTime.getTime()) / 3600 / 24 / 1000);
			} else if (flag.equalsIgnoreCase("hour")) {
				return (int) ((beginTime.getTime() - endTime.getTime()) / 3600 / 1000);
			} else if (flag.equalsIgnoreCase("minute")) {
				return (int) ((beginTime.getTime() - endTime.getTime()) / 60 / 1000);
			} else if (flag.equalsIgnoreCase("second")) {
				return (int) ((beginTime.getTime() - endTime.getTime()) / 1000);
			} else if (flag.equalsIgnoreCase("mill")) {
				return (int) (beginTime.getTime() - endTime.getTime());
			}
		}
		return 0;
	}

	/**
	 * 
	 * dateAdd
	 * 
	 * @描述：日期加
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public java.util.Date dateAdd(Object date, int incNumber, String flag) {
		if (date == null) {
			return null;
		}
		java.util.Date endTime = null;
		if (date instanceof java.util.Date) {
			endTime = (java.util.Date) date;
		} else if (date instanceof java.sql.Date) {
			java.sql.Date sqldate = (java.sql.Date) date;
			endTime = new java.util.Date(sqldate.getTime());
		} else if (date instanceof java.sql.Timestamp) {
			java.sql.Timestamp sqltime = (java.sql.Timestamp) date;
			endTime = new java.util.Date(sqltime.getTime());
		}
		if (flag.equalsIgnoreCase("year")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			int year = calendar.get(Calendar.YEAR);
			calendar.set(Calendar.YEAR, year + incNumber);
			return calendar.getTime();
		} else if (flag.equalsIgnoreCase("month")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			int month = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, month + incNumber);
			return calendar.getTime();
		} else if (flag.equalsIgnoreCase("day")) {
			return new java.util.Date(endTime.getTime() + 3600 * 1000 * 24
					* incNumber);
		} else if (flag.equalsIgnoreCase("hour")) {
			return new java.util.Date(endTime.getTime() + 3600 * 1000
					* incNumber);
		} else if (flag.equalsIgnoreCase("minute")) {
			return new java.util.Date(endTime.getTime() + 60 * 1000 * incNumber);
		} else if (flag.equalsIgnoreCase("second")) {
			return new java.util.Date(endTime.getTime() + 1000 * incNumber);
		} else if (flag.equalsIgnoreCase("mill")) {
			return new java.util.Date(endTime.getTime() + incNumber);
		}
		return null;
	}

	/**
	 * 将Sql Time 转换为字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String sqlTimeToStr(Time time) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"HH:mm:ss");
		java.util.Date date = new java.util.Date(time.getTime());
		return sdf.format(date);

	}
}
