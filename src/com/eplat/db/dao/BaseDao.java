package com.eplat.db.dao;

import com.eplat.db.DBConnection;


/**
 * 
 * @项目名称：eplat
 * @类名称：BaseDao
 * @类描述：所有Dao的父接口
 * @创建人：高洋
 * @创建时间：2010-2-25 下午01:10:06
 * @修改人：高洋
 * @修改时间：2010-2-25 下午01:10:06
 * @修改备注：
 * @version
 */
public interface BaseDao {
	/**
	 * 
	* setDBConnection
	* @描述：给Dao类设置连接对象
	* @param  @return    设定文件
	* @return String    DOM对象   
	* @Exception 异常对象
	 */
	public void setDBConnection(DBConnection dbConnection) ;
	/**
	 * 
	* getDBConnection
	* @描述：dao对象获取数据连接对象
	* @param  @return    设定文件
	* @return String    DOM对象   
	* @Exception 异常对象
	 */
	public DBConnection getDBConnection() ;
}
