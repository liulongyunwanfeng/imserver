package com.eplat.utils;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtil {
	/**
	 * 
	 * mapToBean
	 * 
	 * @描述：将数据从Map转换到数据Bean中
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static Object mapToBean(Map map, Class pojoClass) throws Exception {

		Object bean = null;
		try {
			bean = pojoClass.newInstance();
			PropertyDescriptor[] proNames = PropertysUtil
					.getPropertys(pojoClass); // 取POJO的属性列表
			for (int i = 0; i < proNames.length; i++) {
				String proName = proNames[i].getName();
				Object proValue = map.get(proName);
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.sql.Date")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue instanceof JSONNull) {
						continue;
					}
					String temp = (String) proValue;
					if (temp.equals("")) {
						continue;
					}
					if (temp.trim().length() == 8) {
						temp = temp.substring(0, 4) + "-"
								+ temp.substring(4, 6) + "-"
								+ temp.substring(6, 8);
					}
					proValue = DateTimeUtils.strToSqlDate(temp.trim());
				}
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.sql.Timestamp")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue instanceof JSONNull) {
						continue;
					}
					String temp = (String) proValue;
					if (temp.equals("")) {
						continue;
					}
					if (temp.trim().length() == 10) {
						temp = temp.trim() + " 00:00:00";
					} else if (temp.trim().length() == 8) {
						temp = temp.substring(0, 4) + "-"
								+ temp.substring(4, 6) + "-"
								+ temp.substring(6, 8) + " 00:00:00";
					} else if (temp.trim().length() == 16) {
						temp = temp.trim() + ":00";
					}
					proValue = DateTimeUtils.strToSqlLongDate(temp.trim());

				}
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.math.BigDecimal")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue.equals(" ")) {
						continue;
					}
				}
				try {
					BeanUtils.setProperty(bean, proName, proValue);
				} catch (InvocationTargetException ex1) {
					throw ex1;
				} catch (IllegalAccessException ex1) {
					throw ex1;
				}

			}
		} catch (IllegalAccessException ex) {
			throw ex;
		} catch (InstantiationException ex) {
			throw ex;
		}
		return bean;
	}

	/**
	 * 获取值
	 * 
	 * @param map
	 * @return
	 */
	public static Object getDynaBeanValue(MorphDynaBean map, String proName) {
		try {
			return map.get(proName);
		} catch (Exception e) {
			return null;
		}
	}

	public static Object mapToBean(MorphDynaBean map, Class pojoClass)
			throws Exception {
		Object bean = null;
		try {
			bean = pojoClass.newInstance();
			PropertyDescriptor[] proNames = PropertysUtil
					.getPropertys(pojoClass); // 取POJO的属性列表
			for (int i = 0; i < proNames.length; i++) {
				String proName = proNames[i].getName();
				Object proValue = getDynaBeanValue(map, proName);
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.sql.Date")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue instanceof JSONNull) {
						continue;
					}
					String temp = (String) proValue;
					if (temp.equals("")) {
						continue;
					}
					if (temp.trim().length() == 8) {
						temp = temp.substring(0, 4) + "-"
								+ temp.substring(4, 6) + "-"
								+ temp.substring(6, 8);
					}
					proValue = DateTimeUtils.strToSqlDate(temp.trim());
				}
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.sql.Timestamp")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue instanceof JSONNull) {
						continue;
					}
					String temp = (String) proValue;
					if (temp.equals("")) {
						continue;
					}
					if (temp.trim().length() == 10) {
						temp = temp.trim() + " 00:00:00";
					} else if (temp.trim().length() == 8) {
						temp = temp.substring(0, 4) + "-"
								+ temp.substring(4, 6) + "-"
								+ temp.substring(6, 8) + " 00:00:00";
					}
					proValue = DateTimeUtils.strToSqlLongDate(temp.trim());

				}
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.math.BigDecimal")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue.equals(" ")) {
						continue;
					}
				}
				try {
					BeanUtils.setProperty(bean, proName, proValue);
				} catch (InvocationTargetException ex1) {
					throw ex1;
				} catch (IllegalAccessException ex1) {
					throw ex1;
				}

			}
		} catch (IllegalAccessException ex) {
			throw ex;
		} catch (InstantiationException ex) {
			throw ex;
		}
		return bean;
	}
	public static Object jsonToBean(JSONObject jo, Class pojoClass)
			throws Exception {
		Object bean = null;
		try {
			bean = pojoClass.newInstance();
			PropertyDescriptor[] proNames = PropertysUtil
					.getPropertys(pojoClass); // 取POJO的属性列表
			for (int i = 0; i < proNames.length; i++) {
				String proName = proNames[i].getName();
				Object proValue =null;
				System.out.println(proName);
				if (jo.containsKey(proName)){
					proValue=jo.get(proName);
				} 
				if (proNames[i].getPropertyType().getName().equalsIgnoreCase("java.util.HashMap")){
					if (proValue instanceof net.sf.json.JSONNull){
						continue;
					}
				}
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.sql.Date")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue instanceof JSONNull) {
						continue;
					}
					String temp = (String) proValue;
					if (temp.equals("")) {
						continue;
					}
					if (temp.trim().length() == 8) {
						temp = temp.substring(0, 4) + "-"
								+ temp.substring(4, 6) + "-"
								+ temp.substring(6, 8);
					}
					proValue = DateTimeUtils.strToSqlDate(temp.trim());
				}
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.sql.Timestamp")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue instanceof JSONNull) {
						continue;
					}
					String temp = (String) proValue;
					if (temp.equals("")) {
						continue;
					}
					if (temp.trim().length() == 10) {
						temp = temp.trim() + " 00:00:00";
					} else if (temp.trim().length() == 8) {
						temp = temp.substring(0, 4) + "-"
								+ temp.substring(4, 6) + "-"
								+ temp.substring(6, 8) + " 00:00:00";
					}
					proValue = DateTimeUtils.strToSqlLongDate(temp.trim());

				}
				if (proNames[i].getPropertyType().getName()
						.equalsIgnoreCase("java.math.BigDecimal")) {
					if (proValue == null) {
						continue;
					}
					if (proValue.equals("")) {
						continue;
					}
					if (proValue.equals(" ")) {
						continue;
					}
				}
				try {
					BeanUtils.setProperty(bean, proName, proValue);
				} catch (InvocationTargetException ex1) {
					
				} catch (IllegalAccessException ex1) {
					//throw ex1;
				}

			}
		} catch (IllegalAccessException ex) {
			throw ex;
		} catch (InstantiationException ex) {
			throw ex;
		}
		return bean;
	}

	/**
	 * 
	 * beanToMap
	 * 
	 * @描述：将数据bean的数据转换到Map中
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static Map<String, Object> beanToMap(Object obj) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (obj == null) {
			return null;
		}

		List<Field> fields =PropertysUtil.getFields(obj.getClass());
		if (fields == null) {
			return null;
		}
		for (int i = 0; i < fields.size(); i++) {
			String proName = ""; // 属性名
			Object proValue = ""; // 属性值
			Field field=fields.get(i);
			field.setAccessible(true);
			proName = field.getName();
			proValue = field.get(obj);
			if (field.getType().getName().equalsIgnoreCase("java.sql.Date")) {
				java.sql.Date date=(java.sql.Date)proValue;
				proValue=DateTimeUtils.sqlDateToStr(date);
			} else if (field.getType().getName().equalsIgnoreCase("java.sql.Timestamp")) {
				java.sql.Timestamp date=(java.sql.Timestamp)proValue;
				proValue=DateTimeUtils.sqlDateToLongStr(date);
			} else if (field.getType().getName().equalsIgnoreCase("java.util.Date")) {
				java.util.Date date=(java.util.Date)proValue;
				proValue=DateTimeUtils.dateToLongStr(date);
				String temp=String.valueOf(proValue);
				if (temp.endsWith("00:00:00")){
					proValue= temp.substring(0, 10);
				}
			}
			map.put(proName, proValue);
		}
		return map;
	}
}
