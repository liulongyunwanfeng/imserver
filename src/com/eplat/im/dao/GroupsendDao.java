package com.eplat.im.dao;

import com.eplat.db.BindParamUtils;
import com.eplat.db.dao.AbstractBaseDao;
import com.eplat.db.dao.BaseDao;
import com.eplat.im.domain.GroupsendBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @类名称：GroupsendDao
 * @类描述：群组发送
 * @创建人：代码生成器
 * @创建时间：2017年09月22日 22:24:53
 */
public class GroupsendDao extends AbstractBaseDao implements BaseDao {
	/**
	 * 
	 * insert
	 * 
	 * @描述：新增数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public void insert(Object obj) throws Exception {
		PreparedStatement pstmt = null;
		try {
			GroupsendBean bean = (GroupsendBean) obj;
			String statement = "INSERT INTO IM_GROUP_SEND "
					+ "(GROUP_SEQ_ID,GROUP_MSG_ID,MESSAGE_TYPE,FROM_ID,FROM_NAME,TO_GROUP_ID,TO_GROUP_NAME,CONTENT,SEND_DATE,"
					+ "GROUP_NUM,ACCEPT_NUM,VIEW_NUM)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getGroupseqid());
			pstmt.setString(2, bean.getGroupmsgid());
			pstmt.setString(3, bean.getMessagetype());
			pstmt.setString(4, bean.getFromid());
			pstmt.setString(5, bean.getFromname());
			pstmt.setString(6, bean.getTogroupid());
			pstmt.setString(7, bean.getTogroupname());
			pstmt.setString(8, bean.getContent());
			pstmt.setTimestamp(9, bean.getSenddate());
			pstmt.setInt(10, bean.getGroupnum());
			pstmt.setInt(11, bean.getAcceptnum());
			pstmt.setInt(12, bean.getViewnum());
			pstmt.execute();
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}

	/**
	 * 
	 * insertBatch
	 * 
	 * @描述：批量新增数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void insertBatch(List<GroupsendBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "INSERT INTO IM_GROUP_SEND "
					+ "(GROUP_SEQ_ID,GROUP_MSG_ID,MESSAGE_TYPE,FROM_ID,FROM_NAME,TO_GROUP_ID,TO_GROUP_NAME,CONTENT,SEND_DATE,GROUP_NUM,ACCEPT_NUM,VIEW_NUM)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					GroupsendBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getGroupseqid());
					pstmt.setString(2, bean.getGroupmsgid());
					pstmt.setString(3, bean.getMessagetype());
					pstmt.setString(4, bean.getFromid());
					pstmt.setString(5, bean.getFromname());
					pstmt.setString(6, bean.getTogroupid());
					pstmt.setString(7, bean.getTogroupname());
					pstmt.setString(8, bean.getContent());
					pstmt.setTimestamp(9, bean.getSenddate());
					pstmt.setInt(10, bean.getGroupnum());
					pstmt.setInt(11, bean.getAcceptnum());
					pstmt.setInt(12, bean.getViewnum());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}

	/**
	 * 
	 * update
	 * 
	 * @描述：修改数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void update(Object obj) throws Exception {
		PreparedStatement pstmt = null;
		try {
			GroupsendBean bean = (GroupsendBean) obj;
			String statement = "UPDATE IM_GROUP_SEND SET "
					+ "GROUP_SEQ_ID=?,GROUP_MSG_ID=?,MESSAGE_TYPE=?,FROM_ID=?,FROM_NAME=?,TO_GROUP_ID=?,TO_GROUP_NAME=?,CONTENT=?,"
					+ "SEND_DATE=?,GROUP_NUM=?,ACCEPT_NUM=?,VIEW_NUM=?"
					+ " WHERE GROUP_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getGroupseqid());
			pstmt.setString(2, bean.getGroupmsgid());
			pstmt.setString(3, bean.getMessagetype());
			pstmt.setString(4, bean.getFromid());
			pstmt.setString(5, bean.getFromname());
			pstmt.setString(6, bean.getTogroupid());
			pstmt.setString(7, bean.getTogroupname());
			pstmt.setString(8, bean.getContent());
			pstmt.setTimestamp(9, bean.getSenddate());
			pstmt.setInt(10, bean.getGroupnum());
			pstmt.setInt(11, bean.getAcceptnum());
			pstmt.setInt(12, bean.getViewnum());
			pstmt.setBigDecimal(13, bean.getGroupseqid());
			pstmt.execute();
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}

	/**
	 * 
	 * updateBatch
	 * 
	 * @描述：批量修改数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void updateBatch(List<GroupsendBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "UPDATE IM_GROUP_SEND SET "
					+ "GROUP_SEQ_ID=?,GROUP_MSG_ID=?,MESSAGE_TYPE=?,FROM_ID=?,FROM_NAME=?,TO_GROUP_ID=?,TO_GROUP_NAME=?,CONTENT=?,"
					+ "SEND_DATE=?,GROUP_NUM=?,ACCEPT_NUM=?,VIEW_NUM=?"
					+ " WHERE GROUP_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					GroupsendBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getGroupseqid());
					pstmt.setString(2, bean.getGroupmsgid());
					pstmt.setString(3, bean.getMessagetype());
					pstmt.setString(4, bean.getFromid());
					pstmt.setString(5, bean.getFromname());
					pstmt.setString(6, bean.getTogroupid());
					pstmt.setString(7, bean.getTogroupname());
					pstmt.setString(8, bean.getContent());
					pstmt.setTimestamp(9, bean.getSenddate());
					pstmt.setInt(10, bean.getGroupnum());
					pstmt.setInt(11, bean.getAcceptnum());
					pstmt.setInt(12, bean.getViewnum());
					pstmt.setBigDecimal(13, bean.getGroupseqid());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}

	/**
	 * 
	 * delete
	 * 
	 * @描述：删除数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void delete(Object obj) throws Exception {
		PreparedStatement pstmt = null;
		try {
			GroupsendBean bean = (GroupsendBean) obj;
			String statement = "DELETE FROM IM_GROUP_SEND "
					+ " WHERE GROUP_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getGroupseqid());
			pstmt.execute();
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}

	/**
	 * 
	 * deleteBatch
	 * 
	 * @描述：批量修改数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void deleteBatch(List<GroupsendBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "DELETE FROM  IM_GROUP_SEND "
					+ " WHERE GROUP_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					GroupsendBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getGroupseqid());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}

	/**
	 * 
	 * query
	 * 
	 * @描述：查询数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public List<GroupsendBean> query() throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<GroupsendBean> list = new ArrayList<GroupsendBean>();
		try {
			String statement = "SELECT * FROM IM_GROUP_SEND";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				GroupsendBean bean = new GroupsendBean();
				bean.setGroupseqid(rs.getBigDecimal("GROUP_SEQ_ID"));
				bean.setGroupmsgid(rs.getString("GROUP_MSG_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setTogroupid(rs.getString("TO_GROUP_ID"));
				bean.setTogroupname(rs.getString("TO_GROUP_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setGroupnum(rs.getInt("GROUP_NUM"));
				bean.setAcceptnum(rs.getInt("ACCEPT_NUM"));
				bean.setViewnum(rs.getInt("VIEW_NUM"));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closeResultSet(rs);
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}

	/**
	 * 
	 * queryPK
	 * 
	 * @描述：根据主键查询数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public Object queryPK(Object obj) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			GroupsendBean bean = (GroupsendBean) obj;
			String statement = "SELECT * FROM IM_GROUP_SEND "
					+ " WHERE GROUP_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getGroupseqid());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setGroupseqid(rs.getBigDecimal("GROUP_SEQ_ID"));
				bean.setGroupmsgid(rs.getString("GROUP_MSG_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setTogroupid(rs.getString("TO_GROUP_ID"));
				bean.setTogroupname(rs.getString("TO_GROUP_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setGroupnum(rs.getInt("GROUP_NUM"));
				bean.setAcceptnum(rs.getInt("ACCEPT_NUM"));
				bean.setViewnum(rs.getInt("VIEW_NUM"));
			}
			return bean;
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closeResultSet(rs);
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}

	/**
	 * 
	 * queryByCause
	 * 
	 * @描述：根据主键查询数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public List<GroupsendBean> queryByCause(String sql, Object... sqlParams)
			throws Exception {
		ArrayList<GroupsendBean> list = new ArrayList<GroupsendBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM IM_GROUP_SEND WHERE 1=1 " + sql;
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
				BindParamUtils.bindPreparedObject(pstmt, i + 1, sqlParams[i]);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				GroupsendBean bean = new GroupsendBean();
				bean.setGroupseqid(rs.getBigDecimal("GROUP_SEQ_ID"));
				bean.setGroupmsgid(rs.getString("GROUP_MSG_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setTogroupid(rs.getString("TO_GROUP_ID"));
				bean.setTogroupname(rs.getString("TO_GROUP_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setGroupnum(rs.getInt("GROUP_NUM"));
				bean.setAcceptnum(rs.getInt("ACCEPT_NUM"));
				bean.setViewnum(rs.getInt("VIEW_NUM"));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			this.getDBConnection().closeResultSet(rs);
			this.getDBConnection().closePreparedStatement(pstmt);
		}
	}
}
