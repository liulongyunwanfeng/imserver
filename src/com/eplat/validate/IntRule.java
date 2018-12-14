package com.eplat.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 整数验证
 * @author Administrator
 *
 */
public class IntRule extends ValidateRule {

	@Override
	public String validate(Object value, String label, String rule)
			throws Exception {
		if (value != null) {
			String temp = String.valueOf(value);			
			Pattern pattern = Pattern.compile("^-?\\d+$");
			Matcher match = pattern.matcher(temp);
			if (! match.matches()){
				return String.format(this.getValidMsg(), label);
			}			
		} else {// 检测值为null直接返回
			return String.format(this.getValidMsg(), label);
		}
		return "";
	}

}
