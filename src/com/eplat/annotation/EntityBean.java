package com.eplat.annotation;

import com.eplat.utils.LoggerUtils;
import com.eplat.utils.PropertysUtil;
import com.eplat.utils.StringUtils;
import com.eplat.validate.ValidateUtils;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体Bean
 * 
 * @author Administrator
 *
 */
public class EntityBean implements Serializable, Cloneable {
	private String _entityid = StringUtils.generateID();

	public String get_entityid() {
		return _entityid;
	}

	public void set_entityid(String _entityid) {
		this._entityid = _entityid;
	}

	/**
	 * 转换成字符串显示
	 */
	public String toLogger() throws Exception {
		StringBuffer buffer = new StringBuffer();
		List<Field> fields = PropertysUtil.getFields(this.getClass());
		for (int i = 0; i < fields.size(); i++) {
			FieldInfo fieldInfo = fields.get(i).getAnnotation(FieldInfo.class);
			if (fieldInfo != null) {
				String key = fieldInfo.name();
				if (!StringUtils.hasLength(key)) {
					key = fieldInfo.fieldid();
				}
				if (!StringUtils.hasLength(key)) {
					
					continue;
				}
				Object obj = fields.get(i).get(this);

				if (obj == null) {
					buffer.append(key + "=" + ",");
				} else {
					buffer.append(key + ":" + String.valueOf(obj) + ",");
				}

			} else {
				LoggerUtils.debug("EntityBean", String.format("%s 属性没有加标注，系统日志自动跳过", fields.get(i).getName()));
			}
		}
		String temp=buffer.toString();
		if (temp.endsWith(",")){
			return temp.substring(0,temp.length()-1);
		} else {
			return temp;
		}
	}

	/**
	 * 转换成json对象
	 * 
	 * @return
	 */
	public String toJSON() throws Exception {
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		List<Field> fields = PropertysUtil.getFields(this.getClass());
		for (int i = 0; i < fields.size(); i++) {
			FieldInfo fieldInfo = fields.get(i).getAnnotation(FieldInfo.class);
			if (fieldInfo != null) {
				String key = fieldInfo.jsonkey();
				if (!StringUtils.hasLength(key)) {
					key=fieldInfo.propid();
					if (!StringUtils.hasLength(key)){
						continue;
					}
				}		
				
				Object obj = fields.get(i).get(this);
				data.put(key, obj);
			} else {
				LoggerUtils.debug("EntityBean", String.format("%s 属性没有加标注，转换成JSON自动跳过", fields.get(i).getName()));
			}
		}
		if (data != null && data.size() > 0) {
			JSONObject jo = JSONObject.fromObject(data);
			return jo.toString();
		}
		return "";
	}

	/**
	 * 数据Bean的数据校验
	 * 
	 * @return
	 */
	public List<String> validate() throws Exception {
		return ValidateUtils.validdate(this);
	}
	public String getFieldValue(String fieldName) throws Exception{
		List<Field> targetFields = PropertysUtil.getFields(this.getClass());
		for (int i = 0; i < targetFields.size(); i++) {
			if (targetFields.get(i).getName().equalsIgnoreCase(fieldName)){
				return String.valueOf(targetFields.get(i));
			}
		}
		return "";
		
	}
	/**
	 * 转换到其它数据Bean
	 * 
	 * @param pojoClass
	 * @return
	 * @throws Exception
	 */
	public Object toBean(Class pojoClass) throws Exception {
		Object bean = null;
		bean = pojoClass.newInstance();
		List<Field> targetFields = PropertysUtil.getFields(pojoClass);
		if (targetFields == null || targetFields.size() == 0) {
			return null;
		}
		Map<String, Field> targetMap = new HashMap<String, Field>();
		for (int i = 0; i < targetFields.size(); i++) {
			targetMap.put(targetFields.get(i).getName(), targetFields.get(i));
		}
		List<Field> fields = PropertysUtil.getFields(this.getClass());
		for (int i = 0; i < fields.size(); i++) {
			String fieldName = fields.get(i).getName();
			if ("_entityid".equalsIgnoreCase(fieldName)) {// 唯一键就不需要赋值了
				continue;
			}
			fields.get(i).setAccessible(true);
			Object fieldValue = fields.get(i).get(this);
			if (targetMap.containsKey(fieldName)) {
				targetMap.get(fieldName).setAccessible(true);
				targetMap.get(fieldName).set(bean, fieldValue);
			}
		}
		return bean;
	}

	/**
	 * 对象克隆
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
