package net.sf.json.processors;

import com.eplat.utils.DateTimeUtils;
import net.sf.json.JsonConfig;

/**
 * 
* @项目名称：eplat   
* @类名称：JsonDateToStringProcessorImpl   
* @类描述：为日期类型转换字符串实现的一个JSON处理类   
* @创建人：高洋
* @创建时间：2010-3-21 下午08:20:02   
* @修改人：高洋   
* @修改时间：2010-3-21 下午08:20:02   
* @修改备注：   
* @version
 */
public class JsonDateToStringProcessorImpl implements JsonValueProcessor {

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		try {
			if (value == null)
				return obj;
			if (value instanceof java.util.Date[]) {
				java.util.Date[] dates = (java.util.Date[]) value;
				obj = new String[dates.length];
				for (int i = 0; i < dates.length; i++) {
					obj[i] = DateTimeUtils.dateToStr(dates[i]);
				}
			} else if (value instanceof java.sql.Date[]) {
				java.sql.Date[] dates = (java.sql.Date[]) value;
				obj = new String[dates.length];
				for (int i = 0; i < dates.length; i++) {
					obj[i] = DateTimeUtils.sqlDateToStr(dates[i]);
				}
			} else if (value instanceof java.sql.Timestamp[]) {
				java.sql.Timestamp[] dates = (java.sql.Timestamp[]) value;
				obj = new String[dates.length];
				for (int i = 0; i < dates.length; i++) {
					obj[i] = DateTimeUtils.sqlDateToLongStr(dates[i]);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return obj;

	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		try {
			if (value instanceof java.sql.Date) {
				String str = DateTimeUtils.sqlDateToStr((java.sql.Date) value);
				return str;
			} else if (value instanceof java.sql.Timestamp) {
				String str = DateTimeUtils
						.sqlDateToLongStr((java.sql.Timestamp) value);
				return str;
			} else if (value instanceof java.util.Date) {
				String str = DateTimeUtils.dateToStr((java.util.Date) value);
				return str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
