package com.eplat.db;

import com.eplat.db.rowset.DataRowSet;
import com.eplat.utils.LoggerUtils;
import com.eplat.utils.StringUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDBConnection implements DBConnection {

	/**
	 * 是否启动事务标示，如果为true，表示已经启动了事务
	 */
	private boolean m_InTrans;
	/**
	 * 获取数据库连接
	 */
	private Connection connection;
	/**
	 * 数据库连接配置信息
	 */
	private Configuration configuration;

	public AbstractDBConnection() {
		m_InTrans = false;
	}

	/**
	 * 分解表别名
	 * 
	 * @param sqls
	 * @return
	 */
	public String splitPrefix(String sqls) {
		if (sqls.trim().length() > 1) {
			String[] sqlArray = sqls.split(",");
			if ((sqlArray != null) && (sqlArray.length > 0)) {
				String rtn = "";
				for (int i = 0; i < sqlArray.length; i++) {
					int index = sqlArray[i].indexOf(".");
					if (index > 0) {
						if (rtn.equals("")) {
							rtn = sqlArray[i].substring(index + 1);
						} else {
							rtn = rtn + "," + sqlArray[i].substring(index + 1);
						}
					} else {
						if (rtn.equals("")) {
							rtn = sqlArray[i];
						} else {
							rtn = rtn + "," + sqlArray[i];
						}
					}

				}
				return rtn;
			}
			return sqls;
		} else {
			return sqls;
		}
	}

	public void beginTrans() throws DBException {
		try {
			if (connection == null) {
				connection = this.configuration.getDataSource().getConnection();
			}
			connection.setAutoCommit(false);
			this.m_InTrans = true;
			LoggerUtils.debug("DB", "启动事务成功");
		} catch (SQLException e) {
			LoggerUtils.debug("DB", "启动事务失败：" + e.getMessage());
			throw new DBException(90010, e);
		}
	}

	public void commit() throws DBException {
		try {
			if (this.m_InTrans) {
				connection.commit();
				connection.setAutoCommit(true);
				this.m_InTrans = false;
			}
			LoggerUtils.debug("DB","提交事务成功");
		} catch (SQLException e) {
			LoggerUtils.debug("DB","提交事务失败：" + e.getMessage());
			throw new DBException(90011, e);
		}
	}

	public Connection getConnection() throws DBException {
		try {
			if (this.connection == null) {
				this.connection = this.configuration.getDataSource()
						.getConnection();
			}
			LoggerUtils.debug("DB","ConnectionManager获取连接成功");
			return this.connection;
		} catch (SQLException e) {
			LoggerUtils.debug("DB","ConnectionManager获取连接异常：" + e.getMessage());
			throw new DBException(90012, e);
		}
	}

	public Configuration getConfiguration() throws DBException {
		return this.configuration;
	}

	public boolean inTrans() throws DBException {
		return this.m_InTrans;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * 解析ResultSet到RowSet对象
	 * 
	 * @param rs
	 * @return
	 * @throws DBException
	 */
	public DataRowSet populate(ResultSet rs) throws DBException {
		try {
			DataRowSet rowset = new DataRowSet();
			rowset.populate(rs);
			return rowset;
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	public DataRowSet queryForRs(String statement, Object... params)
			throws DBException {
		LoggerUtils.debug("DB","sqls:" + statement);
		System.out.println(statement);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			long time=System.currentTimeMillis();
			stmt = this.createPreparedStatement(statement);
			BindParamUtils.bindPreparedObjects(stmt, params);
			rs = stmt.executeQuery();
			DataRowSet rowset= this.populate(rs);
			return rowset;
		} catch (Exception e) {
			throw new DBException(90014, e);
		} finally {
			this.closeResultSet(rs);
			this.closePreparedStatement(stmt);
		}
	}

	public void closeCallableStatement(CallableStatement stmt)
			throws DBException {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			throw new DBException(90015, e);
		}
	}

	public void closeConnection() throws DBException {
		try {
			if (this.connection != null) {
				if (this.m_InTrans) {
					this.connection.rollback();
					this.connection.setAutoCommit(true);
					this.m_InTrans = false;
				}
				this.connection.close();
				this.connection = null;
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public void closePreparedStatement(PreparedStatement stmt)
			throws DBException {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public void closeResultSet(ResultSet rs) throws DBException {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public CallableStatement createCallableStatement(String statement)
			throws DBException {
		LoggerUtils.debug("DB","sqls:" + statement);
		try {
			return this.connection.prepareCall(statement);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public PreparedStatement createPreparedStatement(String statement)
			throws DBException {
		try {
			return this.connection.prepareStatement(statement);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public void execute(String statement, Object... params) throws DBException {
		LoggerUtils.debug("DB","sqls:" + statement);
		PreparedStatement stmt = null;
		try {
			stmt = this.createPreparedStatement(statement);
			BindParamUtils.bindPreparedObjects(stmt, params);
			stmt.execute();
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			this.closePreparedStatement(stmt);
		}

	}


	public void rollback() throws DBException {
		try {
			if (this.m_InTrans) {
				this.connection.rollback();
				this.connection.setAutoCommit(true);
				this.m_InTrans = false;
			}
			LoggerUtils.debug("DB","回滚事务成功");
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}


	public int queryForInt(String statement, Object... params)
			throws DBException {
		LoggerUtils.debug("DB","sqls:" + statement);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.createPreparedStatement(statement);
			BindParamUtils.bindPreparedObjects(stmt, params);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			this.closeResultSet(rs);
			this.closePreparedStatement(stmt);
		}
	}


	public Map<String, Object> queryForObject(String statement,
			Object... params) throws DBException {
		LoggerUtils.debug("DB","sqls:" + statement);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.createPreparedStatement(statement);
			BindParamUtils.bindPreparedObjects(stmt, params);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rowMapping(rs);
			} else {
				return null;
			}

		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			this.closeResultSet(rs);
			this.closePreparedStatement(stmt);
		}
	}


	public String getClobString(String sqls, Object... params)
			throws DBException {
		try {
			Clob clob = this.getClob(sqls, params);
			if (clob == null) {
				return "";
			} else {
				return clob.getSubString((long) 1, (int) clob.length());
			}
		} catch (Exception e) {
			throw new DBException(e);
		}

	}


	public InputStream getBlobStream(String sqls, Object... params)
			throws DBException {
		try {
			Blob blob = this.getBlob(sqls, params);
			if (blob == null) {
				return null;
			} else {
				return blob.getBinaryStream();
			}
		} catch (Exception e) {
			throw new DBException(e);
		}

	}

	/**
	 * 解析ResultSet 到行数据
	 * 
	 * @param rs
	 * @return
	 * @throws DBException
	 */
	public Map<String, Object> rowMapping(ResultSet rs) throws DBException {
		try {
			ResultSetMetaData metaData = rs.getMetaData();
			Map<String, Object> row = new HashMap<String, Object>();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i);
				int columnType = metaData.getColumnType(i);
				if ((columnType == java.sql.Types.BIGINT)
						|| (columnType == java.sql.Types.BIT)
						|| (columnType == java.sql.Types.DECIMAL)
						|| (columnType == java.sql.Types.DOUBLE)
						|| (columnType == java.sql.Types.FLOAT)
						|| (columnType == java.sql.Types.INTEGER)
						|| (columnType == java.sql.Types.NUMERIC)
						|| (columnType == java.sql.Types.SMALLINT)
						|| (columnType == java.sql.Types.REAL)
						|| (columnType == java.sql.Types.TINYINT)) {
					row.put(StringUtils.getPropertyName(columnName),
							rs.getBigDecimal(columnName));
				} else if ((columnType == java.sql.Types.CHAR)
						|| (columnType == java.sql.Types.CLOB)
						|| (columnType == java.sql.Types.NCHAR)
						|| (columnType == java.sql.Types.LONGNVARCHAR)
						|| (columnType == java.sql.Types.VARCHAR)) {
					row.put(StringUtils.getPropertyName(columnName),
							rs.getString(columnName));
				} else if ((columnType == java.sql.Types.DATE)) {
					row.put(StringUtils.getPropertyName(columnName),
							rs.getDate(columnName));
				} else if ((columnType == java.sql.Types.TIMESTAMP)) {
					row.put(StringUtils.getPropertyName(columnName),
							rs.getTimestamp(columnName));
				} else if ((columnType == java.sql.Types.TIME)) {
					row.put(StringUtils.getPropertyName(columnName),
							rs.getTime(columnName));
				} else {
					row.put(StringUtils.getPropertyName(columnName),
							rs.getObject(columnName));
				}
			}
			return row;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * 计算取数据的开始行号
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public int computeBeginIndex(int pageIndex, int pageSize) {
		if (pageIndex < 1)
			return 0;
		return (pageIndex - 1) * pageSize;
	}

	/**
	 * 计算结束索引号
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public int computeEndIndex(int pageIndex, int pageSize) {
		if (pageIndex < 1)
			return 0;
		return pageIndex * pageSize;
	}


	public BigDecimal getSequence(String seqName) {
		String statement = "SELECT " + seqName + ".NEXTVAL FROM DUAL";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.createPreparedStatement(statement);

			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getBigDecimal(1);
			} else {
				return null;
			}

		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			this.closeResultSet(rs);
			this.closePreparedStatement(stmt);
		}
	}
}
