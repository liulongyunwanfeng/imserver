package com.eplat.db.rowset;

import java.util.LinkedHashMap;

/**
 * 多数据集
 * 
 * @author Administrator
 * 
 */
public class DataRowSets extends LinkedHashMap<String, DataRowSet> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主查询ID
	 */
	private String m_mainFlag;

	/**
	 * 设置主表标识
	 * 
	 * @param mainFlag
	 */
	public void setMainFlag(String mainFlag) {
		this.m_mainFlag = mainFlag;
	}

	/**
	 * 获取主表标识
	 * 
	 * @return
	 */
	public String getMainFlag() {
		return this.m_mainFlag;
	}

	/**
	 * 获取主表结果集
	 * 
	 * @return
	 */
	public DataRowSet getMainRowSet() {
		return this.get(this.m_mainFlag);
	}
}
