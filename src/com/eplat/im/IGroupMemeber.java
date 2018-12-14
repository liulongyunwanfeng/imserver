package com.eplat.im;

import com.eplat.db.DBConnection;

import java.util.Map;

/**
 * 获取群组成员接口
 * @author Administrator
 *
 */
public interface IGroupMemeber {
	/**
	 * 获取群组用户列表
	 * @param groupid
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getList(String groupid) throws Exception;
	/**
	 * 设置数据库连接
	 * @param connection
	 * @throws Exception
	 */
	public void setConnection(DBConnection connection) throws Exception;
}
