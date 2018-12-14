package com.eplat.db.dao;

import com.eplat.db.DBConnection;

/**
 * 
 * @项目名称：eplat
 * @类名称：AbstractBaseDao
 * @类描述：数据访问对象的抽象类
 * @创建人：高洋
 * @创建时间：2010-2-25 下午01:14:03
 * @修改人：高洋
 * @修改时间：2010-2-25 下午01:14:03
 * @修改备注：
 * @version
 */
public class AbstractBaseDao implements BaseDao {
	// DBConnection
	protected DBConnection dbConnection;

	public DBConnection getDBConnection()  {
		return this.dbConnection;
	}

	public void setDBConnection(DBConnection dbConnection)  {
		this.dbConnection = dbConnection;
	}

}
