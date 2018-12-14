package com.eplat.validate;

/**
 * 所有数据校验的父规则类
 * 
 * @author Administrator
 *
 */
public abstract class ValidateRule {
	private String validMsg;
	
	 public String getValidMsg() {
		return validMsg;
	}

	public void setValidMsg(String validMsg) {
		this.validMsg = validMsg;
	}
	/**
	 * 数据验证
	 * @param rule
	 * @param value
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public abstract String validate(Object value,String label,String rule) throws Exception;
	public static void main(String[] args) {
		 System.out.println(String.format("%s不能为空！", "测试字段"));
		 System.out.println("测试中文(".indexOf("("));
		 System.out.println("test(".substring(0,("test(".indexOf("("))));
		 
	 }

	
}
