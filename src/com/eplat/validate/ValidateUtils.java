package com.eplat.validate;

import com.eplat.annotation.EntityBean;
import com.eplat.annotation.FieldInfo;
import com.eplat.utils.SpringManager;
import com.eplat.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据验证的封装类
 * 
 * @author Administrator
 *
 */
public class ValidateUtils {
	/**
	 * 验证数据Bean，如果返回Null说明所有验证通过，否则存在问题
	 * 
	 * @param entityBean
	 * @return
	 * @throws Exception
	 */
	public static List<String> validdate(EntityBean entityBean)
			throws Exception {
		List<String> dataList = null;
		Field[] fields = entityBean.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			dataList = new ArrayList<String>();
			for (int i = 0; i < fields.length; i++) {
				Object fieldValue = fields[i].get(entityBean);

				FieldInfo fieldInfo = fields[i].getAnnotation(FieldInfo.class);
				if (fieldInfo != null
						&& StringUtils.hasLength(fieldInfo.checktype())) {
					String checkType = fieldInfo.checktype();
					String label = "";
					if (StringUtils.hasLength(fieldInfo.name())) {
						label = fieldInfo.name();
					} else {
						label = fieldInfo.fieldid();
					}
					if (!StringUtils.hasLength(label)) { // 如果字段名称或者字段标题都为空直接跳过不验证
						continue;
					}
					List<String> checkList = splitValidateRule(checkType);
					if (checkList != null && checkList.size() > 0) {
						for (int x = 0; x < checkList.size(); x++) {
							String rule = checkList.get(i);
							String result = validdate(rule, fieldValue, label);
							if (StringUtils.hasLength(result)) {
								dataList.add(result);
							}
						}
					}
				}
			}
		}
		if (dataList != null && dataList.size() > 0) {
			return dataList;
		} else {
			return null;
		}
	}

	/**
	 * 验证数据
	 * 
	 * @param rule
	 *            规则
	 * @param value
	 *            数据值
	 * @param label
	 *            字段名称
	 * @return
	 * @throws Exception
	 */
	public static String validdate(String rule, Object value, String label)
			throws Exception {
		if (!StringUtils.hasLength(rule)) {
			throw new Exception("检测规则不能为空！");
		}
		if (!StringUtils.hasLength(label)) {
			throw new Exception("提示消息不能为空！");
		}
		// 正则表达式，或者其他复杂的自定表达式，比如数据排重
		String beanName = rule;
		String checkRule = "";
		if (rule.indexOf("(") > 0) {
			beanName = rule.substring(0, rule.indexOf("("));

			checkRule = rule
					.substring(rule.indexOf("(") + 1, rule.length() - 1);
		} else {
			beanName = rule;
		}
		beanName = beanName.toLowerCase() + "validate";
		ValidateRule vaildateRule = (ValidateRule) SpringManager
				.getService(beanName);
		return vaildateRule.validate(value, label, checkRule);
	}

	/**
	 * 分解规则，如果规则中有；需要用\\进行转义
	 * 
	 * @param text
	 * @return
	 */
	private static List<String> splitValidateRule(String text) {
		List<String> rtnList = new ArrayList<String>();
		String rule = "";
		char oldchar = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ';' && oldchar != 0 && oldchar != '\\') {
				rtnList.add(rule);
				rule = "";
			} else {
				rule = rule + text.charAt(i);
			}
			oldchar = text.charAt(i);
		}
		return rtnList;
	}
}
