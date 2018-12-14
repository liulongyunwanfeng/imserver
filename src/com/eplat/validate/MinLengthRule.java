package com.eplat.validate;

import com.eplat.utils.StringUtils;
/**
 * 最小长度，一个中文字算两个字符
 * @author Administrator
 *
 */

public class MinLengthRule extends ValidateRule {

	@Override
	public String validate(Object value, String label, String rule)
			throws Exception {
		if (value==null){//检测的值为空，说明为空字符串
			return "";
		}
		if (!StringUtils.isNumeric(String.valueOf(rule))){
			throw new Exception("长度必须为整数！");
		}
		String temp=String.valueOf(value);
		if(StringUtils.charLenght(temp)<Integer.parseInt(rule)){
			return String.format(this.getValidMsg(), label,rule);
		}
		return "";
	}

}
