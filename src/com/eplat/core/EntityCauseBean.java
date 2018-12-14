package com.eplat.core;

/**
 * 实体查询条件Bean
 * 
 * @author Administrator
 *
 */
public class EntityCauseBean {
	private String whereCause;
	private Object[] params = null;

	public String getWhereCause() {
		return whereCause;
	}

	public void setWhereCause(String whereCause) {
		this.whereCause = whereCause;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	

}
