package com.eplat.core;

import com.eplat.annotation.EntityBean;
import com.eplat.db.DBConnection;

/**
 * 服务类接口
 * 
 * @author Administrator
 *
 */
public interface IBizService {
	/**
	 * 获取连接的定义
	 * @return
	 */
	public String getDatasourceId();
	
	public void setDatasourceId(String datasourceId);
	
	/**
	 * 设置服务类的默认连接对象
	 * 
	 * @param dbConnection
	 * @throws BusinessException
	 */
	public void setDBConnection(DBConnection dbConnection)
			throws Exception;

	/**
	 * 获取服务类的默认连接对象
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public DBConnection getDBConnection() throws Exception;

	/**
	 * 启动事务
	 * 
	 * @throws BusinessException
	 */
	public void beginTrans() throws Exception;

	/**
	 * 提交事务
	 * 
	 * @throws BusinessException
	 */
	public void commit() throws Exception;

	/**
	 * 回滚事务
	 * 
	 * @throws BusinessException
	 */
	public void rollback() throws Exception;
	/**
	 * 获取查询条件
	 * @param entity
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public EntityCauseBean getWhereCause(EntityBean entity,ValueObject vo) throws Exception;
}
