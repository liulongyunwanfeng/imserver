package com.eplat.db;

import com.eplat.db.rowset.DataRowSet;
import com.eplat.utils.StringUtils;
import oracle.jdbc.OracleTypes;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * t Oracle 数据库连接的实现
 * 
 * @author Administrator
 * 
 */
public  class OracleConnection extends AbstractDBConnection implements
		DBConnection {

	public DataRowSet queryForRs(String statement, int pageIndex, int pageSize,
			Object... params) throws DBException {
		int beginindex = this.computeBeginIndex(pageIndex, pageSize);
		int endindex = this.computeEndIndex(pageIndex, pageSize);
		String sql = "SELECT * FROM (SELECT ROWNUM irow,tt.* FROM ("
				+ statement + ") tt ) rowtable WHERE " + " rowtable.irow>"
				+ String.valueOf(beginindex) + " AND irow<="
				+ String.valueOf(endindex);
		return this.queryForRs(sql, params);
	}

	public DataRowSet queryForSort(String statement, String sortExp,
			Object... params) throws DBException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM(");
		buffer.append(statement);
		buffer.append(") tt ");
		if ((sortExp != null) && (!sortExp.equalsIgnoreCase(""))) {
			buffer.append("ORDER BY ");
			buffer.append(splitPrefix(sortExp));
		}
		return this.queryForRs(buffer.toString(), params);
	}

	public DataRowSet queryForRs(String statement, String sortExp,
			int pageIndex, int pageSize, Object... params) throws DBException {
		int beginindex = this.computeBeginIndex(pageIndex, pageSize);
		int endindex = this.computeEndIndex(pageIndex, pageSize);
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM (");
		buffer.append("SELECT ROWNUM IROW,TT.* FROM (");
		buffer.append(statement);
		buffer.append(") TT ");
		if ((sortExp != null) && (!sortExp.equalsIgnoreCase(""))) {
			buffer.append(" ORDER BY  ");
			buffer.append(splitPrefix(sortExp));

		}
		buffer.append(") ROWTABLE WHERE ");
		buffer.append(" ROWTABLE.IROW>" + String.valueOf(beginindex));
		buffer.append(" AND IROW<=" + String.valueOf(endindex));
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
			boolean tempTrans = this.inTrans();
			if (!tempTrans)
				this.beginTrans();
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			ResultSet rs = null;
			try {
				String whereCause = "	1=1 ";
				for (int i = 0; i < writeClob.getParamEntity().size(); i++) {
					whereCause = whereCause + " AND "
							+ writeClob.getParamEntity().get(i).getName()
							+ "=?";
				}
				String updateSqls = "UPDATE " + writeClob.getTableName()
						+ " SET " + writeClob.getFieldName() + "=? WHERE "
						+ whereCause;
				int i = 1;
				pstmt = this.createPreparedStatement(updateSqls);
				pstmt.setClob(i, oracle.sql.CLOB.empty_lob());
				for (int j = 0; j < writeClob.getParamEntity().size(); j++) {
					i++;
					BindParamUtils.bindPreparedEntity(pstmt, i, writeClob
							.getParamEntity().get(j));
				}
				pstmt.execute();
				String selectSqls = "SELECT * FROM " + writeClob.getTableName()
						+ " WHERE " + whereCause + " FOR UPDATE";
				pstmt2 = this.createPreparedStatement(selectSqls);
				i = 1;
				for (int j = 0; j < writeClob.getParamEntity().size(); j++) {
					BindParamUtils.bindPreparedEntity(pstmt2, i, writeClob
							.getParamEntity().get(j));
					i++;
				}
				rs = pstmt2.executeQuery();
				if (rs.next()) {
					oracle.sql.CLOB clob = (oracle.sql.CLOB) rs
							.getClob(writeClob.getFieldName());
					BufferedWriter out = new BufferedWriter(
							clob.getCharacterOutputStream());
					out.write(writeClob.getContent(), 0, writeClob.getContent()
							.length());
					out.flush();
					out.close();
				}
				if (!tempTrans) {
					this.commit();
				}
			} catch (Exception e) {
				this.rollback();
				throw new DBException(e);
			} finally {
				this.closeResultSet(rs);
				this.closePreparedStatement(pstmt);
				this.closePreparedStatement(pstmt2);
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
			boolean tempTrans = this.inTrans();
			if (!tempTrans)
				this.beginTrans();
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			ResultSet rs = null;
			try {
				String whereCause = "	1=1 ";
				for (int i = 0; i < writeBlob.getParamEntity().size(); i++) {
					whereCause = whereCause + " AND "
							+ writeBlob.getParamEntity().get(i).getName()
							+ "=?";
				}
				String updateSqls = "UPDATE " + writeBlob.getTableName()
						+ " SET " + writeBlob.getFieldName() + "=? WHERE "
						+ whereCause;
				int i = 1;
				pstmt = this.createPreparedStatement(updateSqls);
				pstmt.setBlob(i, oracle.sql.BLOB.empty_lob());
				for (int j = 0; j < writeBlob.getParamEntity().size(); j++) {
					i++;
					BindParamUtils.bindPreparedEntity(pstmt, i, writeBlob
							.getParamEntity().get(j));
				}
				pstmt.execute();
				String selectSqls = "SELECT * FROM " + writeBlob.getTableName()
						+ " WHERE " + whereCause + " FOR UPDATE";
				pstmt2 = this.createPreparedStatement(selectSqls);
				i = 1;
				for (int j = 0; j < writeBlob.getParamEntity().size(); j++) {
					BindParamUtils.bindPreparedEntity(pstmt2, i, writeBlob
							.getParamEntity().get(j));
					i++;
				}
				rs = pstmt2.executeQuery();
				if (rs.next()) {
					oracle.sql.BLOB blob = (oracle.sql.BLOB) rs
							.getBlob(writeBlob.getFieldName());
					BufferedOutputStream out = new BufferedOutputStream(
							blob.getBinaryOutputStream());
					BufferedInputStream bufferin = new BufferedInputStream(
							writeBlob.getContent());
					int c;
					while ((c = bufferin.read()) != -1) {
						out.write(c);
					}
					bufferin.close();
					out.close();
				}
				if (!tempTrans)
					this.commit();
			} catch (Exception e) {
				this.rollback();
				throw new DBException(e);
			} finally {
				this.closeResultSet(rs);
				this.closePreparedStatement(pstmt);
				this.closePreparedStatement(pstmt2);
			}
		} else {
			throw new DBException("setBlob parameter entity not Exists");
		}

	}

	public InputStream getBlobStream(String sqls, Object... params)
			throws DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = this.createPreparedStatement(sqls);
			BindParamUtils.bindPreparedObjects(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(1);
				InputStream in = blob.getBinaryStream(); // 建立输出流
				/*
				 * FileOutputStream file = new FileOutputStream("c:\\test.jpg");
				 * int len = (int) blob.length(); byte[] buffer = new byte[len];
				 * // 建立缓冲区 while ( (len = in.read(buffer)) != -1) {
				 * file.write(buffer, 0, len); } file.close(); in.close();
				 * return rs.getBlob(1);
				 */
				return in;
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

	public Blob getBlob(String sqls, Object... params) throws DBException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = this.createPreparedStatement(sqls);
			BindParamUtils.bindPreparedObjects(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(1);
				InputStream in = blob.getBinaryStream(); // 建立输出流
				FileOutputStream file = new FileOutputStream("c:\\test.jpg");
				int len = (int) blob.length();
				byte[] buffer = new byte[len]; // 建立缓冲区
				while ((len = in.read(buffer)) != -1) {
					file.write(buffer, 0, len);
				}
				file.close();
				in.close();
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

	/**
	 * 获取执行存储过程的输出参数
	 * 
	 * @param stmt
	 * @param paramEntity
	 * @return
	 * @throws DBException
	 */
	public Object getProcedureOutValue(CallableStatement stmt,
			ParamEntity paramEntity) throws DBException {
		try {
			switch (paramEntity.getType()) {
			case OracleTypes.BIT:
			case OracleTypes.TINYINT:
			case OracleTypes.SMALLINT:
			case OracleTypes.INTEGER:
			case OracleTypes.BIGINT:
			case OracleTypes.FLOAT:
			case OracleTypes.REAL:
			case OracleTypes.DOUBLE:
			case OracleTypes.NUMERIC:
			case OracleTypes.DECIMAL:
				return stmt.getBigDecimal(paramEntity.getIndex());
			case OracleTypes.DATE:
				return stmt.getDate(paramEntity.getIndex());
			case OracleTypes.TIME:
				return stmt.getTime(paramEntity.getIndex());
			case OracleTypes.TIMESTAMP:
				return stmt.getTimestamp(paramEntity.getIndex());
			case OracleTypes.BOOLEAN:
				return stmt.getBoolean(paramEntity.getIndex());
			case OracleTypes.CURSOR:
				ResultSet rs = (ResultSet) stmt.getObject(paramEntity
						.getIndex());
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
				oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(1);
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

	/**
	 * 
	 * getOracleType
	 * 
	 * @描述：获取oracle的数据类型
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	private int getOracleType(String type) throws DBException {
		int rtnType = java.sql.Types.VARCHAR;
		if ((type.equalsIgnoreCase("NUMBER"))
				|| (type.equalsIgnoreCase("FLOAT"))) {
			rtnType = java.sql.Types.DECIMAL;
		} else if ((type.equalsIgnoreCase("DATE"))) {
			rtnType = java.sql.Types.TIMESTAMP;
		} else if ((type.equalsIgnoreCase("TIMESTAMP"))) {
			rtnType = java.sql.Types.TIMESTAMP;
		} else if ((type.equalsIgnoreCase("INTEGER"))) {
			rtnType = java.sql.Types.INTEGER;
		}
		return rtnType;
	}

	@Override
	public List<ColumnBean> getColumns(String uid, String tableName)
			throws DBException {
		ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
		try {
			String statement = "SELECT A.TABLE_NAME TB_NAME,A.COLUMN_NAME COL_NAME,A.DATA_TYPE COL_TYPE,TO_CHAR(A.DATA_LENGTH) COL_LEN,"
					+ " NVL(T.CONSTRAINT_NAME,'0') IS_KEY,replace(replace(D.comments,chr(13),'_'),CHR(10),'_') COMMENTS  FROM USER_TAB_COLUMNS A LEFT JOIN (SELECT U.CONSTRAINT_NAME,"
					+ " U.CONSTRAINT_TYPE ,C.TABLE_NAME,C.COLUMN_NAME FROM USER_CONSTRAINTS U,USER_CONS_COLUMNS C WHERE "
					+ " U.CONSTRAINT_NAME=C.CONSTRAINT_NAME AND U.CONSTRAINT_TYPE='P') T ON (A.TABLE_NAME=T.TABLE_NAME AND "
					+ " A.COLUMN_NAME=T.COLUMN_NAME),USER_COL_COMMENTS D WHERE  A.TABLE_NAME='"
					+ tableName
					+ "' "
					+ " AND D.TABLE_NAME=A.TABLE_NAME AND D.COLUMN_NAME=A.COLUMN_NAME ORDER BY A.COLUMN_ID";
			DataRowSet rs = queryForRs(statement);
			while (rs.next()) {
				ColumnBean bean = new ColumnBean();
				bean.setFieldName(rs.getString("col_name"));
				if (StringUtils.hasLength(rs.getString("COMMENTS"))) {
					bean.setFieldDesc(rs.getString("COMMENTS"));
				} else {
					bean.setFieldDesc(rs.getString("col_name"));
				}

				bean.setIsAutoInc(0);
				if (!rs.getString("IS_KEY").equalsIgnoreCase("0")) {
					bean.setIsPrimaryKey(1);
				} else {
					bean.setIsPrimaryKey(0);
				}
				bean.setFieldLength(rs.getInt("col_len"));
				bean.setFieldType(getOracleType(rs.getString("col_type")));
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
		int beginindex = this.computeBeginIndex(pageIndex, pageSize);
		int endindex = this.computeEndIndex(pageIndex, pageSize);
		String sql = "SELECT * FROM (SELECT ROWNUM irow,tt.* FROM ("
				+ statement + ") tt ) rowtable WHERE " + " rowtable.irow>"
				+ String.valueOf(beginindex) + " AND irow<="
				+ String.valueOf(endindex);
		return this.queryForRs(sql, params);
	}

	@Override
	public DataRowSet queryPageSort(String statement, String sortExp,
			int pageIndex, int pageSize, Object... params) throws DBException {
		int beginindex = this.computeBeginIndex(pageIndex, pageSize);
		int endindex = this.computeEndIndex(pageIndex, pageSize);
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM (");
		buffer.append("SELECT ROWNUM IROW,TT.* FROM (");
		buffer.append(statement);
		buffer.append(") TT ");
		if ((sortExp != null) && (!sortExp.equalsIgnoreCase(""))) {
			buffer.append(" ORDER BY  ");
			buffer.append(splitPrefix(sortExp));

		}
		buffer.append(") ROWTABLE WHERE ");
		buffer.append(" ROWTABLE.IROW>" + String.valueOf(beginindex));
		buffer.append(" AND IROW<=" + String.valueOf(endindex));
		return this.queryForRs(buffer.toString(), params);
	}

	@Override
	public LinkedHashMap<String, Object> execProc(String sql,
			ParamEntity... paramEntities) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
