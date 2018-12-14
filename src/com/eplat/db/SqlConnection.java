package com.eplat.db;

import com.eplat.db.rowset.DataRowSet;
import com.eplat.db.sql.OldSqlServerQueryType;
import com.eplat.db.sql.SqlServerQueryType;
import com.eplat.utils.FileUtils;
import com.eplat.utils.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * sqlserver2000及以下版本的连接
 * 
 * @author Administrator
 * 
 */
public class SqlConnection extends AbstractDBConnection implements DBConnection {

	public DataRowSet queryForRs(String statement, int pageIndex, int pageSize,
			Object... params) throws DBException {
		try {
			OldSqlServerQueryType oldType = new OldSqlServerQueryType(statement);
			String pageSql = oldType.getSQL(pageIndex, pageSize);
			return this.queryForRs(pageSql, params);
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	public DataRowSet queryForSort(String statement, String sortExp,
			Object... params) throws DBException {
		try {
			OldSqlServerQueryType oldType = new OldSqlServerQueryType(statement);
			String pageSql = oldType.getSortSQL(sortExp);
			return this.queryForRs(pageSql, params);
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	public DataRowSet queryForRs(String statement, String sortExp,
			int pageIndex, int pageSize, Object... params) throws DBException {
		try {
			OldSqlServerQueryType oldType = new OldSqlServerQueryType(statement);
			String pageSql = oldType.getSortSQL(pageIndex, pageSize, sortExp);
			return this.queryForRs(pageSql, params);
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	public void setClob(WriteClob writeClob) throws DBException {
		if ((writeClob.getParamEntity() != null)
				&& (writeClob.getParamEntity().size() > 0)) {
			PreparedStatement pstmt = null;
			try {
				String whereCause = "	1=1 ";
				for (int i = 0; i < writeClob.getParamEntity().size(); i++) {
					whereCause = whereCause + " AND "
							+ writeClob.getParamEntity().get(i).getName()
							+ "=?";
				}
				String selectSqls = "UPDATE " + writeClob.getTableName()
						+ " SET " + writeClob.getFieldName() + "=? WHERE "
						+ whereCause;
				pstmt = this.getConnection().prepareStatement(selectSqls);
				int i = 1;
				pstmt.setString(i, writeClob.getContent());
				i++;
				for (int j = 0; j < writeClob.getParamEntity().size(); j++) {
					BindParamUtils.bindPreparedEntity(pstmt, i, writeClob
							.getParamEntity().get(j));
					i++;
				}
				pstmt.execute();
			} catch (Exception e) {
				throw new DBException(e);
			} finally {
				this.closePreparedStatement(pstmt);
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

	public int queryForCount(String statement, Object... params)
			throws DBException {
		try {
			OldSqlServerQueryType oldType = new OldSqlServerQueryType(statement);
			statement = oldType.getTotalSQL();
			return this.queryForInt(statement, params);
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	public byte[] getBlobByte(String sqls, Object... params) throws DBException {
		return null;
	}
	private int getSQLType(String type) throws DBException {
		int rtnType = java.sql.Types.VARCHAR;
		if ((type.equalsIgnoreCase("bigint"))
				|| (type.equalsIgnoreCase("float"))
				|| (type.equalsIgnoreCase("money"))
				|| (type.equalsIgnoreCase("numeric"))
				|| (type.equalsIgnoreCase("real"))
				|| (type.equalsIgnoreCase("decimal"))
				|| (type.equalsIgnoreCase("smallmoney"))) {
			rtnType = java.sql.Types.DECIMAL;
		} else if ((type.equalsIgnoreCase("datetime"))
				|| (type.equalsIgnoreCase("smalldatetime"))) {
			rtnType = java.sql.Types.TIMESTAMP;
		} else if ((type.equalsIgnoreCase("timestamp"))) {
			rtnType = java.sql.Types.TIMESTAMP;
		} else if ((type.equalsIgnoreCase("bit"))
				|| (type.equalsIgnoreCase("int"))
				|| (type.equalsIgnoreCase("smallint"))
				|| (type.equalsIgnoreCase("tinyint"))) {
			rtnType = java.sql.Types.INTEGER;
		}
		return rtnType;
	}
	@Override
	public List<ColumnBean> getColumns(String uid, String tableName)
			throws DBException {
		ArrayList<ColumnBean> list = new ArrayList<ColumnBean>();
		try {
			String statement = " SELECT COL_NAME=a.name,AUTOINC=COLUMNPROPERTY(   a.id,a.name,'IsIdentity'),ISPK=case   when   exists(SELECT   1   FROM   sysobjects  " +
					" where   xtype='PK'   and   name   in   (ELECT   name   FROM   sysindexes   WHERE   indid   in(SELECT   indid   FROM   sysindexkeys  " +
					" WHERE   id   =   a.id   AND   colid=a.colid)))   then   '1'   else   '0'   end,COL_TYPE=b.name,COL_LEN=COLUMNPROPERTY(a.id,a.name,'PRECISION')," +
					" COL_DEC=isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0),ALLOW_NULL=case   when   a.isnullable=1   then   '1'else   '0'   end," +
					"DEFAULT_VALUE=isnull(e.text,''), COMMENTS=isnull(g.[value],'')  FROM   syscolumns   a left   join   systypes   b   on   a.xusertype=b.xusertype" +
					" inner   join   sysobjects   d   on   a.id=d.id     and   d.xtype='U'   and     d.name<>'dtproperties' " +
					"left   join   syscomments   e   on   a.cdefault=e.id left   join   sys.extended_properties   g   on   a.id=g.major_id   and   a.colid=g.minor_id" +
					" left   join   sys.extended_properties   f   on   d.id=f.major_id   and   f.minor_id=0 where   d.name='"+tableName+"' order   by   a.id,a.colorder";
			DataRowSet rs = queryForRs(statement);
			while (rs.next()) {
				ColumnBean bean = new ColumnBean();
				bean.setFieldName(rs.getString("COL_NAME"));
				if (StringUtils.hasLength(rs.getString("COMMENTS"))) {
					bean.setFieldDesc(rs.getString("COMMENTS"));
				} else {
					bean.setFieldDesc(rs.getString("COL_NAME"));
				}

				bean.setIsAutoInc(0);
				if (!rs.getString("ISPK").equalsIgnoreCase("0")) {
					bean.setIsPrimaryKey(1);
				} else {
					bean.setIsPrimaryKey(0);
				}
				bean.setFieldLength(rs.getInt("COL_LEN"));
				bean.setFieldType(getSQLType(rs.getString("col_type")));
				list.add(bean);
			}
			return list;
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	public BigDecimal getSequence(String seqName) {
		String statement = "insert into SeqT_0101001 (SeqVal) values ('a');select @@identity";
		int data = this.queryForInt(statement);
		this.execute("delete from SeqT_0101001 WITH (READPAST)");
		return new BigDecimal(data);
	}

	
	@Override
	public DataRowSet queryPage(String statement, int pageIndex, int pageSize,
			Object... params) throws DBException {
		try {
			SqlServerQueryType oldType = new SqlServerQueryType(statement);
			String pageSql = oldType.getSQL(pageIndex, pageSize);
			return this.queryForRs(pageSql, params);
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	@Override
	public DataRowSet queryPageSort(String statement, String sortExp,
			int pageIndex, int pageSize, Object... params) throws DBException {
		try {
			SqlServerQueryType oldType = new SqlServerQueryType(statement);
			String pageSql = oldType.getSortSQL(pageIndex, pageSize, sortExp);
			return this.queryForRs(pageSql, params);
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	@Override
	public LinkedHashMap<String, Object> execProc(String sql,
			ParamEntity... paramEntities) throws Exception {
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


}
