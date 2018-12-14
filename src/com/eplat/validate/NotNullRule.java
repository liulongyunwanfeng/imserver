package com.eplat.validate;

import com.eplat.utils.StringUtils;

/**
 * 非空判断
 * 
 * @author Administrator
 *
 */
public class NotNullRule extends ValidateRule {

	@Override
	public String validate(Object value, String label, String rule)
			throws Exception {
		if (value instanceof String) {
			if (!StringUtils.hasLength(String.valueOf(value))) {
				return String.format(this.getValidMsg(), label);
			}
		} else {
			if (value == null) {
				return String.format(this.getValidMsg(), label);
			}
		}
		return "";
	}

}
