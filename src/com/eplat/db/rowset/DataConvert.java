package com.eplat.db.rowset;

import com.eplat.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;

/**
 * 数据转换
 * 
 * @author Administrator
 * 
 */
public class DataConvert {
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static BigDecimal ObjectToBigDecimal(Object obj) {
		if (obj == null) {
			return null;
		}
		return new BigDecimal(obj.toString().trim());
	}

	public static Blob ObjectToBlob(Object obj, int type) throws Exception {
		if (type != 2004) {
			throw new Exception("Datatype Mismatch");
		}
		Blob blob = (Blob) obj;
		if (blob == null) {
			return null;
		}
		return blob;
	}

	public static boolean ObjectToBoolean(Object obj) {
		if (obj == null) {
			return false;
		}
		if ((obj instanceof Boolean)) {
			return ((Boolean) obj).booleanValue();
		}
		Double double1 = new Double(obj.toString());
		return double1.compareTo(new Double(0.0D)) != 0;
	}

	public static byte ObjectToByte(Object obj) {
		if (obj == null) {
			return 0;
		}
		return new Byte(obj.toString()).byteValue();
	}

	public static byte[] ObjectToBytes(Object obj, int type) throws Exception {
		if (!isBinary(type)) {
			throw new Exception("Data Type Mismatch");
		}
		return (byte[]) obj;
	}

	public static Clob ObjectToClob(Object obj, int type) throws Exception {
		if (type != 2005) {
			throw new Exception("Datatype Mismatch");
		}
		Clob clob = (Clob) obj;
		if (clob == null) {
			return null;
		}
		return clob;
	}

	public static java.sql.Date StringToDate(String obj) {
		return new java.sql.Date(Long.parseLong(obj));
	}

	public static java.sql.Date ObjectToDate(Object obj, int type) throws Exception{
		if (obj == null) {
			return null;
		}
		switch (type) {
		case 91:
		case 93:
			long l = ((java.util.Date) obj).getTime();
			return new java.sql.Date(l);
		case -1:
		case 1:
		case 12:
			try {
				return DateTimeUtils.strToSqlDate(obj.toString());
			} catch (Exception e) {
				throw new Exception("String can't to Date" + obj.toString());
			}

		}
		return null;
	}

	public static double ObjectToDouble(Object obj, int type) {
		if (obj == null) {
			return 0.0D;
		}
		return new Double(obj.toString().trim()).doubleValue();
	}

	public static float ObjectToFloat(Object obj, int type) {
		if (obj == null) {
			return 0.0F;
		}
		return new Float(obj.toString().trim()).floatValue();
	}

	public static int ObjectToInt(Object obj, int type) {
		if (obj == null) {
			return 0;
		}
		return new Integer(obj.toString().trim()).intValue();
	}

	public static long ObjectToLong(Object obj, int type) {
		if (obj == null) {
			return 0L;
		}
		return new Long(obj.toString().trim()).longValue();
	}

	public static short ObjectToShort(Object obj, int type) {
		if (obj == null) {
			return 0;
		}
		return new Short(obj.toString().trim()).shortValue();
	}

	public static String ObjectToString(Object obj, int type) throws Exception {
		try {
			if (obj == null) {
				return null;
			}
			if (type == 2005) {
				Clob clob = (Clob) obj;
				return clob.getSubString(1, (int) clob.length());
			} else if (type == 91) {// 日期类型
				if (obj instanceof java.sql.Timestamp) {
					return DateTimeUtils
							.sqlDateToLongStr((java.sql.Timestamp) obj);
				} else {
					return DateTimeUtils.sqlDateToStr((java.sql.Date) obj);
				}
			} else if (type == 93) {// 日期时间类型
				return DateTimeUtils.sqlDateToLongStr((java.sql.Timestamp) obj);
			}
			return obj.toString();
		} catch (Exception e) {
			throw e;
		}
	}

	public static Time StringToTime(String obj) {
		return new Time(Long.parseLong(obj));
	}

	public static Time ObjectToTime(Object obj, int type) {
		if (obj == null) {
			return null;
		}
		switch (type) {
		case 92:
			return (Time) obj;

		case 93:
			long l = ((java.sql.Timestamp) obj).getTime();
			return new Time(l);
		case -1:
		case 1:
		case 12:
			try {
				java.util.Date _date = null;
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
						"HH:mm:ss");
				_date = sdf.parse(obj.toString());
				return new Time(_date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Timestamp StringToTimestamp(String obj) {
		return new Timestamp(Long.parseLong(obj));
	}

	public static Timestamp ObjectToTimestamp(Object obj, int type) throws Exception {
		if (obj == null) {
			return null;
		}
		if (obj.toString().equals("")) {
			return null;
		}
		switch (type) {
		case 93:
			return (Timestamp) obj;
		case 92:
			long l = ((Time) obj).getTime();
			return new Timestamp(l);
		case 91:
			long ll = ((java.util.Date) obj).getTime();
			return new Timestamp(ll);
		case -1:
		case 1:
		case 12:
			try {
				return DateTimeUtils.strToSqlLongDate(obj.toString());
			} catch (Exception ex) {
				throw new Exception("ObjectTOTimestamp Failed on value("
						+ obj.toString().trim() + ")");
			}
		}
		return null;
	}

	public static Object convertNumeric(Object obj, int i, int j) throws Exception {
		if (i == j) {
			return obj;
		}
		if ((!isNumeric(j)) && (!isString(j))) {
			throw new Exception("1.Datatype Mismatch: " + j);
		}
		if (obj == null)
			return null;
		if (obj.toString().equals("")) {
			return null;
		}
		switch (j) {
		case -7:
			Integer integer = new Integer(obj.toString().trim());
			return integer.equals(new Integer(0)) ? new Boolean(false)
					: new Boolean(true);
		case -6:
			return new Byte(obj.toString().trim());
		case 5:
			return new Short(obj.toString().trim());
		case 4:
			return new Integer(obj.toString().trim());
		case -5:
			return new Long(obj.toString().trim());
		case 2:
		case 3:
			return new BigDecimal(obj.toString().trim());
		case 6:
		case 7:
			return new Float(obj.toString().trim());
		case 8:
			return new Double(obj.toString().trim());
		case -1:
		case 1:
		case 12:
			return new String(obj.toString());
		case -4:
		case -3:
		case -2:
		case 0:
		case 9:
		case 10:
		case 11:
		}
		throw new Exception("2.Data Type Mismatch: " + j);
	}

	public static boolean isNumeric(int i) {
		switch (i) {
		case -7:
		case -6:
		case -5:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			return true;
		case -4:
		case -3:
		case -2:
		case -1:
		case 0:
		case 1:
		}
		return false;
	}

	public static boolean isString(int i) {
		switch (i) {
		case -1:
		case 1:
		case 12:
			return true;
		}
		return false;
	}

	public static boolean isBinary(int i) {
		switch (i) {
		case -4:
		case -3:
		case -2:
		case 2004:
			return true;
		}
		return false;
	}

	public static Object convertTemporal(Object obj, int i, int j) throws Exception {
		if (i == j) {
			return obj;
		}
		if ((isNumeric(j)) || ((!isString(j)) && (!isTemporal(j)))) {
			throw new Exception("Datatype Mismatch");
		}

		if (obj == null) {
			return null;
		}
		if (obj.toString().equals("")) {
			return null;
		}

		switch (j) {
		case 91:
			if (i == 93) {
				return new java.sql.Date(((Timestamp) obj).getTime());
			}
			throw new Exception("Data Type Mismatch");
		case 93:
			if (i == 92) {
				return new Timestamp(((Time) obj).getTime());
			}
			return new Timestamp(((java.util.Date) obj).getTime());
		case 92:
			if (i == 93) {
				return new Time(((Timestamp) obj).getTime());
			}
			throw new Exception("Data Type Mismatch");
		case -1:
		case 1:
		case 12:
			return new String(obj.toString());
		}
		return null;
	}

	public static boolean isTemporal(int i) {
		switch (i) {
		case 91:
		case 92:
		case 93:
			return true;
		}
		return false;
	}
}
