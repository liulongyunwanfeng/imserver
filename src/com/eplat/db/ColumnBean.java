package com.eplat.db;

/**
 * 数据库列的获取
 * 
 * @author Administrator
 * 
 */
public class ColumnBean {
	// 字段名称
	private String fieldName;
	// 字段类型
	private int fieldType;
	// 字段长度
	private long fieldLength;
	// 字段描述
	private String fieldDesc;
	// 属性名称
	private String propertyName;
	// 属性类型
	private String propertyType;
	// 是否是主键
	private int isPrimaryKey;
	// 是否是自增列
	private int isAutoInc;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	public long getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(long fieldLength) {
		this.fieldLength = fieldLength;
	}

	public String getFieldDesc() {
		return fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public int getIsPrimaryKey() {
		return isPrimaryKey;
	}

	public void setIsPrimaryKey(int isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public int getIsAutoInc() {
		return isAutoInc;
	}

	public void setIsAutoInc(int isAutoInc) {
		this.isAutoInc = isAutoInc;
	}

}
