package net.sf.json.processors;

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
public class JsonBigDecimalToStringProcessorImpl implements JsonValueProcessor {

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		try {
			if (value == null)
				return obj;
			if (value instanceof java.math.BigDecimal[]) {
				java.math.BigDecimal[] dates = (java.math.BigDecimal[]) value;
				obj = new String[dates.length];
				for (int i = 0; i < dates.length; i++) {
					obj[i] = String.valueOf(dates[i]);
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
			if (value instanceof java.math.BigDecimal) {
				String str = String.valueOf((java.math.BigDecimal) value);
				return str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
