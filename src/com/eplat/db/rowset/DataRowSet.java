package com.eplat.db.rowset;

import com.eplat.utils.StringUtils;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;
import net.sf.json.JSONArray;

import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRowSet implements Serializable, Cloneable,
		TemplateSequenceModel {

	private static final long serialVersionUID = 1L;
	private List<DataRow> dataList = new ArrayList<DataRow>();
	private RowSetMetaDataImpl rowsetMetaData;
	private int rowCount = -1;// 总行数
	private int cursorPos = -1; // 当前坐标
	private boolean firstFlag = false;// 表示移动到首行调用

	public RowSetMetaDataImpl getRowsetMetaData() {
		return rowsetMetaData;
	}

	public void setRowsetMetaData(RowSetMetaDataImpl rowsetMetaData) {
		this.rowsetMetaData = rowsetMetaData;
	}

	/**
	 * 获取行数据
	 * 
	 * @param rowIndex
	 * @return
	 */
	public DataRow getRow(int rowIndex) {
		if (rowIndex > this.dataList.size()) {
			return null;
		} else {
			return this.dataList.get(rowIndex);
		}
	}

	public List<DataRow> getDataList() {
		return dataList;
	}

	public void setDataList(List<DataRow> dataList) {
		this.dataList = dataList;
	}

	/**
	 * 根据索引号获取列明
	 * 
	 * @param columnIndex
	 * @return
	 * @throws DBException
	 */
	public String getColumnNameByIdx(int columnIndex) throws Exception {
		try {
			return rowsetMetaData.getColumnName(columnIndex);
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	/**
	 * 解析结果集填充到dataList中
	 * 
	 * @param rs
	 */
	public synchronized void populate(ResultSet rs) throws Exception {
		try {
			if (rs == null) {
				throw new Exception("传入的ResultSet为空异常！");
			}
			this.rowsetMetaData = new RowSetMetaDataImpl();
			this.initMetaData(this.rowsetMetaData, rs.getMetaData());
			int j = this.rowsetMetaData.getColumnCount(); // 总列数
			DataRow localRow = null;
			int i = 0;
			while (rs.next()) {
				localRow = new DataRow();
				for (int k = 1; k <= j; k++) {
					Object localObject;
					localObject = rs.getObject(k);
					if ((localObject instanceof Blob))
						localObject = new SerialBlob((Blob) localObject);
					else if ((localObject instanceof Clob))
						localObject = new SerialClob((Clob) localObject);
					localRow.put(this.getColumnNameByIdx(k), localObject);
				}
				i++;
				localRow.setRowsetMetaData(this.rowsetMetaData);
				this.dataList.add(localRow);
			}
			this.rowCount = i;
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	/**
	 * 初始化结构定义
	 * 
	 * @param rowsetDataMeta
	 * @param rsMeta
	 * @throws SQLException
	 */
	private void initMetaData(RowSetMetaDataImpl rowsetDataMeta,
			ResultSetMetaData rsMeta) throws SQLException {
		int i = rsMeta.getColumnCount();
		rowsetDataMeta.setColumnCount(i);
		for (int j = 1; j <= i; j++) {
			rowsetDataMeta.setAutoIncrement(j, rsMeta.isAutoIncrement(j));
			//rowsetDataMeta.setCaseSensitive(j, rsMeta.isCaseSensitive(j));
			rowsetDataMeta.setCurrency(j, rsMeta.isCurrency(j));
			rowsetDataMeta.setNullable(j, rsMeta.isNullable(j));
			rowsetDataMeta.setSigned(j, rsMeta.isSigned(j));
			rowsetDataMeta.setSearchable(j, rsMeta.isSearchable(j));
			rowsetDataMeta.setColumnDisplaySize(j,
					rsMeta.getColumnDisplaySize(j));
			rowsetDataMeta.setColumnLabel(j, rsMeta.getColumnLabel(j));
			String columnName = rsMeta.getColumnName(j);
			if (StringUtils.hasLength(columnName)) {// 处理列名称为大写
				rowsetDataMeta.setColumnName(j, rsMeta.getColumnName(j)
						.toUpperCase());
			} else {
				rowsetDataMeta.setColumnName(j, rsMeta.getColumnName(j));
			}
			rowsetDataMeta.setSchemaName(j, rsMeta.getSchemaName(j));
			if ((rsMeta.getColumnType(j) != Types.BLOB)
					&& (rsMeta.getColumnType(j) != Types.CLOB)) {
				rowsetDataMeta.setPrecision(j, rsMeta.getPrecision(j));
			}
			if (rsMeta.getScale(j) < 0) {
				rowsetDataMeta.setScale(j, 10);
			} else {
				rowsetDataMeta.setScale(j, rsMeta.getScale(j));
			}
			rowsetDataMeta.setTableName(j, rsMeta.getTableName(j));
			rowsetDataMeta.setCatalogName(j, rsMeta.getCatalogName(j));
			rowsetDataMeta.setColumnType(j, rsMeta.getColumnType(j));
			rowsetDataMeta.setColumnTypeName(j, rsMeta.getColumnTypeName(j));
		}
	}

	/**
	 * 移动到下一条记录
	 * 
	 * @return
	 */
	public boolean next() {
		if (this.firstFlag) {
			this.firstFlag = false;
			return true;
		}
		if (this.cursorPos < this.rowCount - 1) {
			this.cursorPos += 1;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 移动到首行
	 * 
	 * @return
	 */
	public boolean first() {
		if (this.rowCount <= 0) {
			return false;
		} else {
			this.cursorPos = 0;
			this.firstFlag = true;
			return true;
		}
	}

	/**
	 * 移动到前一行
	 * 
	 * @return
	 */
	public boolean previous() {
		if (this.rowCount <= 0) {
			return false;
		} else if (this.cursorPos <= 0) {
			return false;
		} else {
			this.cursorPos -= 1;
			return true;
		}
	}

	public boolean last() {
		if (this.cursorPos >= this.rowCount - 1) {
			return false;
		} else {
			this.cursorPos = this.rowCount - 1;
			return true;
		}
	}

	/**
	 * 获取当前行数据
	 * 
	 * @return
	 */
	public DataRow getCurrentRow() throws Exception {
		if (this.rowCount == -1) {
			return null;
		}
		if (this.cursorPos == -1) {
			throw new Exception("please call next  first");
		}
		return this.dataList.get(this.cursorPos);
	}

	/**
	 * 删除当前行并返回当前行
	 * 
	 * @return
	 */
	public DataRow removeCurrentRow() {
		if ((this.cursorPos >= 0) && (this.cursorPos < this.rowCount)) {
			return this.removeRow(this.cursorPos);
		} else {
			return null;
		}
	}

	/**
	 * 删除指定的行
	 * 
	 * @param rowIndex
	 * @return
	 */
	public DataRow removeRow(int rowIndex) {
		if ((rowIndex >= 0) && (rowIndex < this.rowCount)) {
			if (rowIndex < this.cursorPos) {
				this.previous();
			} else if (rowIndex == this.cursorPos) {
				if ((rowIndex == this.rowCount - 1) && (rowIndex == 0)) {// 只要一条记录删除以后，直接将位置设置为无效
																			// -1
					this.cursorPos = -1;
				} else if (rowIndex == this.rowCount - 1) {// 如果删除的是最有一行，当前坐标向前移动一行
					this.previous();
				}
			}
			this.rowCount -= 1;
			return this.dataList.remove(rowIndex);
		}
		return null;
	}

	/**
	 * 转换为JSON字符串
	 * 
	 * @return
	 */
	public String toJson() {
		return JSONArray.fromObject(this.dataList).toString();
	}

	/**
	 * 转换为XML结构
	 * 
	 * @param rowsetName
	 * @return
	 */
	public String toXml(String rowsetName) throws Exception{
		StringBuffer buffer = new StringBuffer();
		if (StringUtils.hasLength(rowsetName)) {
			buffer.append("<rowset name=\"" + rowsetName + "\">");
		} else {
			buffer.append("<rowset>");
		}
		if ((this.dataList != null) && (this.dataList.size() > 0)) {
			for (int i = 0; i < this.dataList.size(); i++) {
				DataRow row = this.dataList.get(i);
				buffer.append(row.toXml(i + 1));
			}
		}
		buffer.append("</rowset>");
		return buffer.toString();
	}

	public Object getObject(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getObject(columnName);
		} else {
			return null;
		}
	}

	public BigDecimal getBigDecimal(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getBigDecimal(columnName);
		} else {
			return null;
		}
	}

	public Blob getBlob(String columnName) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getBlob(columnName);
		} else {
			return null;
		}

	}

	public boolean getBoolean(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getBoolean(columnName);
		} else {
			return false;
		}
	}

	public byte getByte(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getByte(columnName);
		} else {
			return 0;
		}
	}

	public byte[] getBytes(String columnName) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getBytes(columnName);
		} else {
			return null;
		}
	}

	public Clob getClob(String columnName) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getClob(columnName);
		} else {
			return null;
		}
	}

	public Date getDate(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getDate(columnName);
		} else {
			return null;
		}
	}

	public double getDouble(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getDouble(columnName);
		} else {
			return 0.0D;
		}
	}

	public float getFloat(String columnName)throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getFloat(columnName);
		} else {
			return 0.0F;
		}
	}

	public int getInt(String columnName) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getInt(columnName);
		} else {
			return 0;
		}
	}

	public long getLong(String columnName) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getLong(columnName);
		} else {
			return 0L;
		}
	}

	public short getShort(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getShort(columnName);
		} else {
			return 0;
		}
	}

	public String getString(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getString(columnName);
		} else {
			return null;
		}
	}

	public Time getTime(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getTime(columnName);
		} else {
			return null;
		}
	}

	public Timestamp getTimestamp(String columnName) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			return row.getTimestamp(columnName);
		} else {
			return null;
		}
	}

	public void setBigDecimal(String columnName, BigDecimal dec) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setBigDecimal(columnName, dec);
		}
	}

	public void setBlob(String columnName, Blob b) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setBlob(columnName, b);
		}
	}

	public void setBoolean(String columnName, boolean flag) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setBoolean(columnName, flag);
		}
	}

	public void setByte(String columnName, byte byte0)  throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setByte(columnName, byte0);
		}
	}

	public void setBytes(String columnName, byte[] abyte0)  throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setBytes(columnName, abyte0);
		}
	}

	public void setClob(String columnName, Clob c)throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setClob(columnName, c);
		}
	}

	public void setDate(String columnName, Date date) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setDate(columnName, date);
		}
	}

	public void setDouble(String columnName, double d) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setDouble(columnName, d);
		}
	}

	public void setFloat(String columnName, float f) throws Exception {
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setFloat(columnName, f);
		}
	}

	public void setInt(String columnName, int j)  throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setInt(columnName, j);
		}
	}

	public void setLong(String columnName, long l) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setLong(columnName, l);
		}
	}

	public void setShort(String columnName, Short l) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setShort(columnName, l);
		}
	}

	public void setString(String columnName, String s) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setString(columnName, s);
		}
	}

	public void setTime(String columnName, Time time) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setTime(columnName, time);
		}
	}

	public void setTimestamp(String columnName, Timestamp timestamp) throws Exception{
		DataRow row = this.getCurrentRow();
		if (row != null) {
			row.setTimestamp(columnName, timestamp);
		}
	}


	public TemplateModel get(int arg0) throws TemplateModelException {

		return new SimpleHash(this.dataList.get(arg0));
	}


	public int size() throws TemplateModelException {
		return this.dataList.size();
	}
}
