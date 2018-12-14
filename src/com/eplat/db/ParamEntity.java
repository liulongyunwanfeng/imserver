package com.eplat.db;

import java.io.Serializable;

/**
 * 参数承载实体
 * 
 * @author Administrator
 * 
 */
public class ParamEntity implements Serializable, Cloneable {
	private static final long serialVersionUID = 7345122089233772840L;
	private int index;// 参数索引号
	private String name;// 参数名称
	private Object value;// 参数值
	private String flag;// 参数标示
	private int type;// 参数类型

	public ParamEntity clone() {
		ParamEntity paramEntity = new ParamEntity();
		paramEntity.index = this.index;
		paramEntity.name = this.name;
		paramEntity.value = this.value;
		paramEntity.flag = this.flag;
		paramEntity.type = this.type;
		return paramEntity;
	}

	public ParamEntity() {

	}

	public ParamEntity(int index, String name, Object value, String flag, int type) {
		this.index = index;
		this.name = name;
		this.value = value;
		this.flag = flag;
		this.type = type;
	}

	public ParamEntity(int index, String name, Object value, int type) {
		this.index = index;
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
