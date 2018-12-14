package com.eplat.db;

import com.eplat.db.rowset.DataRowSet;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库连接封装接口
 * 
 * @author Administrator
 *
 */
public interface DBConnection {
	/**
	 * 获取JDBC数据连接
	 * 
	 * @return
	 * @throws DBException
	 */
	public Connection getConnection() throws Exception;

	/**
	 * 获取数据库连接的配置
	 * 
	 * @return
	 * @throws DBException
	 */
	public Configuration getConfiguration() throws Exception;

	/**
	 * 设置数据库连接的配置信息
	 * 
	 * @param configuration
	 */
	public void setConfiguration(Configuration configuration);

	/**
	 * 启动数据库事务
	 * 
	 * @throws DBException
	 */
	public void beginTrans() throws Exception;

	/**
	 * 提交数据库事务
	 * 
	 * @throws DBException
	 */
	public void commit() throws Exception;

	/**
	 * 回滚数据库事务
	 * 
	 * @throws DBException
	 */
	public void rollback() throws Exception;

	/**
	 * 是否在数据库事务中
	 * 
	 * @return
	 * @throws DBException
	 */
	public boolean inTrans() throws Exception;

	/**
	 * 执行查询，并带参数绑定的操作
	 * 
	 * @param statement
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public DataRowSet queryForRs(String statement, Object... params)
			throws Exception;

	/**
	 * 执行分页查询 带参数绑定
	 * 
	 * @param statement
	 * @param pageIndex
	 * @param pageSize
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public DataRowSet queryPage(String statement, int pageIndex, int pageSize,
			Object... params) throws Exception;

	/**
	 * 带参数的排序查询
	 * 
	 * @param statement
	 * @param sortExp
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public DataRowSet queryForSort(String statement, String sortExp,
			Object... params) throws Exception;

	/**
	 * 执行分页排序查询带参数
	 * 
	 * @param statement
	 * @param sortExp
	 * @param pageIndex
	 * @param pageSize
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public DataRowSet queryPageSort(String statement, String sortExp,
			int pageIndex, int pageSize, Object... params) throws Exception;

	/**
	 * 执行查询返回整数
	 * 
	 * @param statement
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public int queryForInt(String statement, Object... params)
			throws Exception;

	/**
	 * 执行查询返回汇总数
	 * 
	 * @param statement
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public int queryForCount(String statement, Object... params)
			throws Exception;

	/**
	 * 查询数据返回对象
	 * 
	 * @param statement
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public Map<String, Object> queryForObject(String statement,
			Object... params) throws Exception;

	/**
	 * 执行SQL语句更新，不返回结果
	 * 
	 * @param statement
	 * @param params
	 * @throws DBException
	 */
	public void execute(String statement, Object... params) throws Exception;

	/**
	 * 创建PreparedStatement
	 * 
	 * @param statement
	 * @return
	 * @throws DBException
	 */
	public PreparedStatement createPreparedStatement(String statement)
			throws Exception;

	/**
	 * 关闭PreparedStatement
	 * 
	 * @param stmt
	 * @throws DBException
	 */
	public void closePreparedStatement(PreparedStatement stmt)
			throws Exception;

	/**
	 * 创建存储过程的调用对象
	 * 
	 * @param statement
	 * @return
	 * @throws DBException
	 */
	public CallableStatement createCallableStatement(String statement)
			throws Exception;

	/**
	 * 关闭存储过程调用的对象
	 * 
	 * @param stmt
	 * @throws DBException
	 */
	public void closeCallableStatement(CallableStatement stmt)
			throws Exception;

	/**
	 * 关闭数据库连接
	 * 
	 * @throws DBException
	 */
	public void closeConnection() throws Exception;

	/**
	 * 关闭ResultSet对象
	 * 
	 * @param rs
	 * @throws DBException
	 */
	public void closeResultSet(ResultSet rs) throws Exception;

	/**
	 * 写入文本
	 * 
	 * @param writeClob
	 * @throws DBException
	 */
	public void setClob(WriteClob writeClob) throws Exception;

	/**
	 * 从Clob字段中获取文本，返回字符串
	 * 
	 * @param sqls
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public String getClobString(String sqls, Object... params)
			throws Exception;

	/**
	 * 获取文本，返回Clob数据
	 * 
	 * @param sqls
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public Clob getClob(String sqls, Object... params) throws Exception;

	/**
	 * 写入二进制数据到数据库
	 * 
	 * @param writeBlob
	 * @throws DBException
	 */
	public void setBlob(WriteBlob writeBlob) throws Exception;

	/**
	 * 从Blob获取输入流
	 * 
	 * @param sqls
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public InputStream getBlobStream(String sqls, Object... params)
			throws Exception;

	/**
	 * 从Blob获取byte数组
	 * 
	 * @param sqls
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public byte[] getBlobByte(String sqls, Object... params) throws Exception;

	/**
	 * 获取Blob字段数据
	 * 
	 * @param sqls
	 * @param params
	 * @return
	 * @throws DBException
	 */
	public Blob getBlob(String sqls, Object... params) throws Exception;

	/**
	 * 执行存储过程
	 * 
	 * @param sql
	 * @param paramEntities
	 * @return
	 * @throws DBException
	 */
	public LinkedHashMap<String, Object> executeProcedure(String sql,
			ParamEntity... paramEntities) throws Exception;
	/**
	 * 执行存储不返回游标
	 * @param sql
	 * @param paramEntities
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, Object> execProc(String sql,
			ParamEntity... paramEntities) throws Exception;
	/**
	 * 获取SEQ的值
	 * 
	 * @param sqlName
	 * @return
	 */
	public BigDecimal getSequence(String seqName);

	/**
	 * 
	 * getColumns
	 * 
	 * @描述：获取数据库所有的列
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public List<ColumnBean> getColumns(String uid, String tableName)
			throws Exception;
}
