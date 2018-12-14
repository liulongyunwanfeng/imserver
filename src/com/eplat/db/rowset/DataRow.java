package com.eplat.db.rowset;

import com.eplat.utils.DateTimeUtils;
import com.eplat.utils.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;

import javax.sql.rowset.RowSetMetaDataImpl;
import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedHashMap;

/**
 * 行对象
 * 
 * @author Administrator
 * 
 */
public class DataRow extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	private RowSetMetaDataImpl rowsetMetaData;

	public RowSetMetaDataImpl getRowsetMetaData() {
		return rowsetMetaData;
	}

	public void setRowsetMetaData(RowSetMetaDataImpl rowsetMetaData) {
		this.rowsetMetaData = rowsetMetaData;
	}

	/**
	 * 获取列的数据类型
	 * 
	 * @param columnName
	 * @return
	 * @throws DBException
	 */
	private int getColumnType(String columnName) throws Exception {
		try {
			int i = this.rowsetMetaData.getColumnCount();
			for (int j = 1; j <= i; j++) {
				String str = this.rowsetMetaData.getColumnName(j);
				if ((str != null) && (columnName.equalsIgnoreCase(str)))
					return this.rowsetMetaData.getColumnType(j);
			}
			throw new Exception("Column name not Exists:" + columnName);
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	/**
	 * 检查列明是否存在
	 * 
	 * @param columnName
	 */
	public void checkColumnName(String columnName) throws Exception {
		if (!this.containsKey(columnName)) {
			throw new Exception("Column Name not Exists(" + columnName + ")");
		}
	}

	/**
	 * 获取列对象
	 * 
	 * @param columnName
	 * @return
	 * @throws DBException
	 */
	public Object getColumnObject(String columnName) throws Exception {
		String column = columnName.toUpperCase();
		if (this.containsKey(column)) {
			return this.get(column);
		} else {
			throw new Exception("Column Name not Exists " + columnName);
		}
	}

	public Object getObject(String columnName) throws Exception {
		return this.getColumnObject(columnName);
	}

	public BigDecimal getBigDecimal(String columnName) throws Exception {
		Object obj = this.getColumnObject(columnName);
		return DataConvert.ObjectToBigDecimal(obj);
	}

	public Blob getBlob(String columnName) throws Exception {
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToBlob(obj, t);
	}

	public boolean getBoolean(String columnName)  throws Exception{
		Object obj = getColumnObject(columnName);
		return DataConvert.ObjectToBoolean(obj);
	}

	public byte getByte(String columnName) throws Exception{
		Object obj = getColumnObject(columnName);
		return DataConvert.ObjectToByte(obj);
	}

	public byte[] getBytes(String columnName) throws Exception {
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToBytes(obj, t);
	}

	public Clob getClob(String columnName) throws Exception{
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToClob(obj, t);
	}

	public Date getDate(String columnName) throws Exception{
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToDate(obj, t);
	}

	public double getDouble(String columnName) throws Exception {
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToDouble(obj, t);
	}

	public float getFloat(String columnName) throws Exception {
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToFloat(obj, t);
	}

	public int getInt(String columnName) throws Exception{
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToInt(obj, t);
	}

	public long getLong(String columnName) throws Exception{
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToLong(obj, t);
	}

	public short getShort(String columnName) throws Exception {
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToShort(obj, t);
	}

	public String getString(String columnName) throws Exception {
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToString(obj, t);
	}
	/**
	 * 加入控制返回空字符串而不返回null
	 * @param columnName
	 * @return
	 */
	public String getStringEx(String columnName) throws Exception{
		String data=getString(columnName);
		if (!StringUtils.hasLength(data)){
			return "";
		} else {
			return data;
		}
	}
	public Time getTime(String columnName) throws Exception {
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToTime(obj, t);
	}

	public Timestamp getTimestamp(String columnName) throws Exception{
		Object obj = getColumnObject(columnName);
		int t = getColumnType(columnName);
		return DataConvert.ObjectToTimestamp(obj, t);
	}

	public void setBigDecimal(String columnName, BigDecimal dec) throws Exception{
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		this.put(column, dec);
	}

	public void setBlob(String columnName, Blob b) throws Exception {
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		this.put(column, b);
	}

	public void setBoolean(String columnName, boolean flag) throws Exception{
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertNumeric(new Boolean(flag), -7,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setByte(String columnName, byte byte0) throws Exception {
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertNumeric(new Byte(byte0), -7,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setBytes(String columnName, byte[] abyte0) throws Exception {
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		if (!DataConvert.isBinary(getColumnType(column))) {
			throw new Exception("Data Type Mismatch");
		}
		this.put(column, abyte0);
	}

	public void setClob(String columnName, Clob c) throws Exception {
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		this.put(column, c);
	}

	public void setDate(String columnName, Date date) throws Exception {
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertTemporal(date, 91,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setDouble(String columnName, double d) throws Exception{
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertNumeric(new Double(d), 8,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setFloat(String columnName, float f) throws Exception{
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertNumeric(new Float(f), 7,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setInt(String columnName, int j) throws Exception{
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertNumeric(new Integer(j), 4,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setLong(String columnName, long l) throws Exception{
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertNumeric(new Long(l), -5,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setShort(String columnName, Short l) throws Exception{
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertNumeric(new Short(l), 5,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setString(String columnName, String s) throws Exception {
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		this.put(column, s);
	}

	public void setTime(String columnName, Time time) throws Exception {
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertTemporal(time, 92,
				getColumnType(column));
		this.put(column, obj);
	}

	public void setTimestamp(String columnName, Timestamp timestamp)  throws Exception{
		String column = columnName.toUpperCase();
		this.checkColumnName(column);
		Object obj = DataConvert.convertTemporal(timestamp, 93,
				getColumnType(column));
		this.put(column, obj);
	}

	/**
	 * 将行数据转换为XML
	 * 
	 * @param index
	 * @return
	 */
	public String toXml(int index) throws Exception {
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<row index=\"" + String.valueOf(index) + "\"");
			buffer.append((char) 13 + (char) 10);
			int i = this.rowsetMetaData.getColumnCount();
			for (int j = 1; j <= i; j++) {
				String str = this.rowsetMetaData.getColumnName(j);
				if (str == null) {
					continue;
				}
				if ((this.rowsetMetaData.getColumnType(j) == 2004)
						|| (this.rowsetMetaData.getColumnType(j) == 2006)
						|| (this.rowsetMetaData.getColumnType(j) == 70)
						|| (this.rowsetMetaData.getColumnType(j) == 2002)
						|| (this.rowsetMetaData.getColumnType(j) == 2003)) {// BLOB等类型忽略
					continue;
				}
				buffer.append("< column name=\"" + str + "\">");

				String columnValue = "";
				if (this.getObject(str) != null) {
					try {
						switch (this.rowsetMetaData.getColumnType(j)) {
						case 91:
							columnValue = DateTimeUtils.sqlDateToStr(this.getDate(str));
							break;
						case 92:
							columnValue=DateTimeUtils.sqlTimeToStr(this.getTime(str));
							break;
						case 93:
							columnValue = DateTimeUtils.sqlDateToLongStr(this.getTimestamp(str));
							break;
						default:
							columnValue=this.getString(str);
						}
					} catch (Exception e) {
						
					}
				}
				buffer.append(StringEscapeUtils.escapeXml(columnValue));
				buffer.append("</column>");
				buffer.append((char) 13 + (char) 10);
			}
			buffer.append("</row>");
			buffer.append((char) 13 + (char) 10);
			return buffer.toString();
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	
	
}
