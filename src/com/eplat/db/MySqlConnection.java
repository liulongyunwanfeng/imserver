package com.eplat.db;

import com.eplat.db.rowset.DataRowSet;
import com.eplat.utils.FileUtils;
import com.eplat.utils.StringUtils;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class MySqlConnection extends AbstractDBConnection implements
		DBConnection {

	public DataRowSet queryForRs(String statement, int pageIndex, int pageSize,
			Object... params) throws DBException {
		int beginindex = (pageIndex - 1) * pageSize;
		String sql = "SELECT * FROM (" + statement + ") tt LIMIT "
				+ String.valueOf(beginindex) + "," + String.valueOf(pageSize);
		return this.queryForRs(sql, params);
	}

	public DataRowSet queryForSort(String statement, String sortExp,
			Object... params) throws DBException {
		String sql = "";
		if (StringUtils.hasLength(sortExp)) {
			sql = "SELECT * FROM (" + statement + ") tt ORDER BY "
					+ splitPrefix(sortExp);
		} else {
			sql = "SELECT * FROM (" + statement + ") tt";
		}
		return this.queryForRs(sql, params);
	}

	public DataRowSet queryForRs(String statement, String sortExp,
			int pageIndex, int pageSize, Object... params) throws DBException {
		int beginindex = (pageIndex - 1) * pageSize;
		String sql = "SELECT * FROM (" + statement + ") tt  ";
		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		if ((sortExp != null) && (!sortExp.equalsIgnoreCase(""))) {
			buffer.append(" order by ");
			buffer.append(splitPrefix(sortExp));
		}
		buffer.append(" limit " + String.valueOf(beginindex) + ","
				+ String.valueOf(pageSize));
		return this.queryForRs(buffer.toString(), params);
	}

	public int queryForCount(String statement, Object... params)
			throws DBException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT COUNT(*) FROM (");
		buffer.append(statement);
		buffer.append(") TT");
		return this.queryForInt(buffer.toString(), params);
	}

	public void setClob(WriteClob writeClob) throws DBException {
		if ((writeClob.getParamEntity() != null)
				&& (writeClob.getParamEntity().size() > 0)) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				String whereCause = "	1=1 ";
				for (int i = 0; i < writeClob.getParamEntity().size(); i++) {
					whereCause = whereCause + " AND "
							+ writeClob.getParamEntity().get(i).getName()
							+ "=?";
				}
				String selectSqls = "SELECT * FROM " + writeClob.getTableName()
						+ " WHERE " + whereCause;
				pstmt = this.getConnection()
						.prepareStatement(selectSqls,
								ResultSet.TYPE_FORWARD_ONLY,
								ResultSet.CONCUR_UPDATABLE);
				int i = 1;
				for (int j = 0; j < writeClob.getParamEntity().size(); j++) {
					BindParamUtils.bindPreparedEntity(pstmt, i, writeClob
							.getParamEntity().get(j));
					i++;
				}
				rs = pstmt.executeQuery();
				if (rs.next()) {
					rs.updateBytes(writeClob.getFieldName(), writeClob
							.getContent().getBytes("utf-8"));
					rs.updateRow();// commit change

				}
			} catch (Exception e) {
				throw new DBException(e);
			}
		} else {
			throw new DBException("setClob parameter entity not Exists");
		}

	}

	public Clob getClob(String sqls, Object... params) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = this.createPreparedStatement(sqls);
			BindParamUtils.bindPreparedObjects(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getClob(1);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			this.closeResultSet(rs);
			this.closePreparedStatement(pstmt);
		}
	}

	public void setBlob(WriteBlob writeBlob) throws DBException {
		if ((writeBlob.getParamEntity() != null)
				&& (writeBlob.getParamEntity().size() > 0)) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				String whereCause = "	1=1 ";
				for (int i = 0; i < writeBlob.getParamEntity().size(); i++) {
					whereCause = whereCause + " AND "
							+ writeBlob.getParamEntity().get(i).getName()
							+ "=?";
				}
				String selectSqls = "SELECT * FROM " + writeBlob.getTableName()
						+ " WHERE " + whereCause;
				pstmt = this.getConnection()
						.prepareStatement(selectSqls,
								ResultSet.TYPE_FORWARD_ONLY,
								ResultSet.CONCUR_UPDATABLE);
				int i = 1;
				for (int j = 0; j < writeBlob.getParamEntity().size(); j++) {
					BindParamUtils.bindPreparedEntity(pstmt, i, writeBlob
							.getParamEntity().get(j));
					i++;
				}
				rs = pstmt.executeQuery();
				if (rs.next()) {
					byte[] bytes = FileUtils.copyToByteArray(writeBlob
							.getContent());
					rs.updateBytes(writeBlob.getFieldName(), bytes);
					rs.updateRow();// commit change

				}
			} catch (Exception e) {
				throw new DBException(e);
			}
		} else {
			throw new DBException("setBlob parameter entity not Exists");
		}

	}

	public Blob getBlob(String sqls, Object... params) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = this.createPreparedStatement(sqls);
			BindParamUtils.bindPreparedObjects(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getBlob(1);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			this.closeResultSet(rs);
			this.closePreparedStatement(pstmt);
		}
	}

	public Object getProcedureOutValue(CallableStatement stmt,
			ParamEntity paramEntity) throws DBException {
		try {
			switch (paramEntity.getType()) {
			case Types.BIT:
			case Types.TINYINT:
			case Types.SMALLINT:
			case Types.INTEGER:
			case Types.BIGINT:
			case Types.FLOAT:
			case Types.REAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
			case Types.DECIMAL:
				return stmt.getBigDecimal(paramEntity.getIndex());
			case Types.DATE:
				return stmt.getDate(paramEntity.getIndex());
			case Types.TIME:
				return stmt.getTime(paramEntity.getIndex());
			case Types.TIMESTAMP:
				return stmt.getTimestamp(paramEntity.getIndex());
			case Types.BOOLEAN:
				return stmt.getBoolean(paramEntity.getIndex());
			case Types.OTHER:
				ResultSet rs = stmt.getResultSet();
				DataRowSet rowset = new DataRowSet();
				rowset.populate(rs);
				this.closeResultSet(rs);
				return rowset;
			default:
				return stmt.getString(paramEntity.getIndex());
			}
		} catch (Exception e) {
			throw new DBException(e);
		}

	}

	public LinkedHashMap<String, Object> executeProcedure(String sql,
			ParamEntity... paramEntities) throws DBException {
		LinkedHashMap<String, Object> rtnMap = new LinkedHashMap<String, Object>();
		CallableStatement callableStatement = null;
		try {
			callableStatement = this.createCallableStatement(sql);
			if ((paramEntities != null) && (paramEntities.length > 0)) {
				for (int i = 0; i < paramEntities.length; i++) {
					if ((InOutFlag.OUT_PARAM.equalsIgnoreCase(paramEntities[i]
							.getFlag()))
							&& (paramEntities[i].getType() == Types.OTHER)) {// 输出游标参数不需要注册
						continue;
					}
					BindParamUtils.bindProcedureEntity(callableStatement,
							paramEntities[i].getIndex(), paramEntities[i]);
				}
			}
			callableStatement.execute();
			if ((paramEntities != null) && (paramEntities.length > 0)) {
				for (int i = 0; i < paramEntities.length; i++) {
					if (InOutFlag.OUT_PARAM.equalsIgnoreCase(paramEntities[i]
							.getFlag())) {
						Object outValue = this.getProcedureOutValue(
								callableStatement, paramEntities[i]);
						rtnMap.put(paramEntities[i].getName(), outValue);
					}
				}
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			this.closeCallableStatement(callableStatement);
		}
		return rtnMap;
	}

	public byte[] getBlobByte(String sqls, Object... params) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = this.createPreparedStatement(sqls);
			BindParamUtils.bindPreparedObjects(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob(1);
				if (blob == null)
					return null;
				InputStream in = blob.getBinaryStream(); // 建立输出流
				int len = (int) blob.length();
				byte[] buffer = new byte[len]; // 建立缓冲区
				in.read(buffer);
				in.close();
				return buffer;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			this.closeResultSet(rs);
			this.closePreparedStatement(pstmt);
		}

	}

	private int getType(String type) throws DBException {
		int rtnType = java.sql.Types.VARCHAR;
		if ((type.equalsIgnoreCase("NUMBER"))
				|| (type.equalsIgnoreCase("FLOAT"))) {
			rtnType = java.sql.Types.DECIMAL;
		} else if ((type.equalsIgnoreCase("DATE"))
				|| (type.equalsIgnoreCase("DATETIME"))) {
			rtnType = java.sql.Types.TIMESTAMP;
		} else if ((type.equalsIgnoreCase("TIMESTAMP"))) {
			rtnType = java.sql.Types.TIMESTAMP;
		} else if ((type.equalsIgnoreCase("INTEGER"))
				|| (type.equalsIgnoreCase("INT"))) {
			rtnType = java.sql.Types.INTEGER;
		}
		return rtnType;
	}

	@Override
	public List<ColumnBean> getColumns(String uid, String tableName)
			throws DBException {
		String dbName = this.getConfiguration().getAssistInfo("dbname");
		String statement = "select t.column_comment ,t.column_name,case COLUMN_KEY "
				+ " when 'PRI' then 1 when 'MUL' then 1  else 0 end as IS_KEY,T.data_type,"
				+ " IFNULL(IFNULL(t.CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION),10) AS  COL_LEN "
				+ " from information_schema.columns t where t.TABLE_schema='"
				+ dbName + "' AND T.TABLE_NAME='" + tableName + "'";

		ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
		try {

			DataRowSet rs = queryForRs(statement);

			while (rs.next()) {
				ColumnBean bean = new ColumnBean();
				bean.setFieldName(rs.getString("COLUMN_NAME"));
				bean.setFieldDesc(rs.getString("COLUMN_COMMENT"));
				bean.setIsAutoInc(0);
				if (!rs.getString("IS_KEY").equalsIgnoreCase("0")) {
					bean.setIsPrimaryKey(1);
				} else {
					bean.setIsPrimaryKey(0);
				}
				bean.setFieldLength(rs.getLong("COL_LEN"));
				bean.setFieldType(getType(rs.getString("DATA_TYPE")));
				list.add(bean);
			}
			return list;
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	@Override
	public DataRowSet queryPage(String statement, int pageIndex, int pageSize,
			Object... params) throws DBException {
		int beginindex = (pageIndex - 1) * pageSize;
		String sql = "SELECT * FROM (" + statement + ") tt  ";
		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		buffer.append(" limit " + String.valueOf(beginindex) + ","
				+ String.valueOf(pageSize));
		return this.queryForRs(buffer.toString(), params);
	}

	@Override
	public DataRowSet queryPageSort(String statement, String sortExp,
			int pageIndex, int pageSize, Object... params) throws DBException {
		int beginindex = (pageIndex - 1) * pageSize;
		String sql = "SELECT * FROM (" + statement + ") tt  ";
		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		if ((sortExp != null) && (!sortExp.equalsIgnoreCase(""))) {
			buffer.append(" order by ");
			buffer.append(splitPrefix(sortExp));
		}
		buffer.append(" limit " + String.valueOf(beginindex) + ","
				+ String.valueOf(pageSize));
		return this.queryForRs(buffer.toString(), params);
	}

	@Override
	public LinkedHashMap<String, Object> execProc(String sql,
			ParamEntity... paramEntities) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
