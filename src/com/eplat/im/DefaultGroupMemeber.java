package com.eplat.im;

import com.eplat.db.DBConnection;
import com.eplat.im.dao.GroupMemberDao;

import java.util.Map;

/**
 * 默认用户群组
 * @author Administrator
 *
 */
public class DefaultGroupMemeber implements IGroupMemeber {
	private DBConnection connection=null;

	private GroupMemberDao groupMemberDao;

	public GroupMemberDao getGroupMemberDao() {
		return groupMemberDao;
	}

	public void setGroupMemberDao(GroupMemberDao groupMemberDao) {
		this.groupMemberDao = groupMemberDao;
	}


	public Map<String,String> getList(String groupid) throws Exception {
		// TODO Auto-generated method stub

		if(null==groupid){
			throw new Exception("群组参数不能为空");
		}
		groupMemberDao.setDBConnection(this.connection);
		return  groupMemberDao.getList(groupid);

	}

	@Override
	public void setConnection(DBConnection connection) throws Exception {
		this.connection=connection;
		
	}

}
