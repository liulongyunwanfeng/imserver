package com.eplat.db;

import java.math.BigDecimal;
import java.sql.*;



/**
 * 绑定Statement 参数的封装类
 * 
 * @author Administrator
 * 
 */
public class BindParamUtils {

	/**
	 * 绑定PreparedStatement参数
	 * 
	 * @param stmt
	 * @param params
	 * @throws DBException
	 */
	public static void bindPreparedObjects(PreparedStatement stmt,
			Object... params) throws Exception {
		if ((params == null) || (params.length == 0)) {
			return;
		} else {
			for (int i = 0; i < params.length; i++) {
				bindPreparedObject(stmt, i + 1, params[i]);
			}
		}
	}

	/**
	 * 绑定PreparedStatement 单个参数
	 * 
	 * @param stmt
	 * @param index
	 * @param param
	 * @throws DBException
	 */
	public static void bindPreparedObject(PreparedStatement stmt, int index,
			Object obj) throws Exception {
		try {
			if (obj == null) {// 如果参数为空直接绑定Null值
				stmt.setNull(index, Types.INTEGER);
			} else {
				if (obj instanceof String) {// 字符串
					stmt.setString(index, obj.toString());
				} else if (obj instanceof Integer) {// 整数
					stmt.setInt(index, ((Integer) obj).intValue());
				} else if (obj instanceof Long) {
					stmt.setLong(index, ((Long) obj).longValue());
				} else if (obj instanceof Short) {
					stmt.setShort(index, ((Short) obj).shortValue());
				} else if (obj instanceof Boolean) {
					stmt.setBoolean(index, ((Boolean) obj).booleanValue());
				} else if (obj instanceof BigDecimal) {
					stmt.setBigDecimal(index, (BigDecimal) obj);
				} else if (obj instanceof Double) {
					stmt.setDouble(index, ((Double) obj).doubleValue());
				} else if (obj instanceof Float) {
					stmt.setFloat(index, ((Float) obj).floatValue());
				} else if (obj instanceof java.util.Date) {
					java.util.Date date = (java.util.Date) obj;
					stmt.setTimestamp(index, new Timestamp(date.getTime()));
				} else if (obj instanceof Byte) {
					stmt.setByte(index, ((Byte) obj).byteValue());
				} else if (obj instanceof Blob) {
					stmt.setBlob(index, ((Blob) obj));
				} else if (obj instanceof Clob) {
					stmt.setClob(index, ((Clob) obj));
				} else if (obj instanceof Date) {
					stmt.setDate(index, (Date) obj);
				} else if (obj instanceof Time) {
					stmt.setTime(index, (Time) obj);
				}
			}
		} catch (SQLException e) {
			throw new Exception( "绑定参数错误。", e);
		}

	}

	/**
	 * 绑定PreparedStatement 的ParamEntity参数
	 * 
	 * @param stmt
	 * @param index
	 * @param entity
	 * @throws DBException
	 */
	public static void bindPreparedEntity(PreparedStatement stmt, int index,
			ParamEntity entity) throws Exception {
		try {
			if (entity.getValue() == null) {
				stmt.setNull(index, entity.getType());
			} else {
				bindPreparedObject(stmt, index, entity.getValue());
			}

		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	/**
	 * 绑定存储过程参数
	 * 
	 * @param stmt
	 * @param index
	 * @param entity
	 * @throws DBException
	 */
	public static void bindProcedureEntity(CallableStatement stmt, int index,
			ParamEntity entity) throws Exception {
		try {
			if (InOutFlag.IN_PARAM.equalsIgnoreCase(entity.getFlag())) {// 输入参数
				if (entity.getValue() == null) {
					stmt.setNull(index, entity.getType());
				} else {
					bindPreparedObject(stmt, index, entity.getValue());
				}
			} else {// 绑定输出参数
				stmt.registerOutParameter(index, entity.getType());
			}

		} catch (SQLException e) {
			throw new Exception(e);
		}
	}
}
