package com.eplat.validate;

import com.eplat.utils.StringUtils;

import java.math.BigDecimal;

/**
 * 最大值判断
 * @author Administrator
 *
 */
public class MaxRule extends ValidateRule {

	@Override
	public String validate(Object value, String label, String rule)
			throws Exception {
		if (!StringUtils.canNumber(String.valueOf(value))||!StringUtils.canNumber(String.valueOf(rule))){
			throw new Exception("最大值判断必须是数字或浮点数！");
		}
		BigDecimal checkValue=new BigDecimal(String.valueOf(value));
		BigDecimal targetValue=new BigDecimal(String.valueOf(rule));
		if (checkValue.compareTo(targetValue)>0){
			return String.format(this.getValidMsg(), label,rule);
		}		
		return "";
	}

}
