package com.eplat.db;

import java.util.ArrayList;
import java.util.List;

/**
 * 写入CLOB对象用于参数传递
 * 
 * @author Administrator
 * 
 */
public class WriteClob {
	private String tableName;
	private String fieldName;
	private String content;
	private List<ParamEntity> paramEntity = new ArrayList<ParamEntity>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ParamEntity> getParamEntity() {
		return paramEntity;
	}

	public void setParamEntity(List<ParamEntity> paramEntity) {
		this.paramEntity = paramEntity;
	}
	/**
	 * 添加参数
	 * @param entity
	 */
	public void addEntity(ParamEntity entity){
		if (this.paramEntity==null){
			this.paramEntity=new ArrayList<ParamEntity>(); 
		}
		this.paramEntity.add(entity);
	}

}
