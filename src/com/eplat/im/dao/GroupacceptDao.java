package com.eplat.im.dao;

import com.eplat.db.BindParamUtils;
import com.eplat.db.dao.AbstractBaseDao;
import com.eplat.db.dao.BaseDao;
import com.eplat.im.domain.GroupacceptBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @类名称：GroupacceptDao
 * @类描述：群组消息接收
 * @创建人：代码生成器
 * @创建时间：2017年09月22日 22:24:50
 */
public class GroupacceptDao extends AbstractBaseDao implements BaseDao {
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
			GroupacceptBean bean = (GroupacceptBean) obj;
			String statement = "INSERT INTO IM_GROUP_ACCEPT "
					+ "(ACCEPT_SEQ_ID,GROUP_MSG_ID,MESSAGE_TYPE,FROM_ID,FROM_NAME,TO_GROUP_ID,TO_GROUP_NAME,TO_ID,TO_NAME,CONTENT,SEND_DATE,ACCPET_DATE,ACCEPT_STATE,VIEW_DATE,VIEW_STATE)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getAcceptseqid());
			pstmt.setString(2, bean.getGroupmsgid());
			pstmt.setString(3, bean.getMessagetype());
			pstmt.setString(4, bean.getFromid());
			pstmt.setString(5, bean.getFromname());
			pstmt.setString(6, bean.getTogroupid());
			pstmt.setString(7, bean.getTogroupname());
			pstmt.setString(8, bean.getToid());
			pstmt.setString(9, bean.getToname());
			pstmt.setString(10, bean.getContent());
			pstmt.setTimestamp(11, bean.getSenddate());
			pstmt.setTimestamp(12, bean.getAccpetdate());
			pstmt.setString(13, bean.getAcceptstate());
			pstmt.setTimestamp(14, bean.getViewdate());
			pstmt.setString(15, bean.getViewstate());
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

	public void insertBatch(List<GroupacceptBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "INSERT INTO IM_GROUP_ACCEPT "
					+ "(ACCEPT_SEQ_ID,GROUP_MSG_ID,MESSAGE_TYPE,FROM_ID,FROM_NAME,TO_GROUP_ID,TO_GROUP_NAME,TO_ID,TO_NAME,CONTENT,SEND_DATE,ACCPET_DATE,ACCEPT_STATE,VIEW_DATE,VIEW_STATE)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					GroupacceptBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getAcceptseqid());
					pstmt.setString(2, bean.getGroupmsgid());
					pstmt.setString(3, bean.getMessagetype());
					pstmt.setString(4, bean.getFromid());
					pstmt.setString(5, bean.getFromname());
					pstmt.setString(6, bean.getTogroupid());
					pstmt.setString(7, bean.getTogroupname());
					pstmt.setString(8, bean.getToid());
					pstmt.setString(9, bean.getToname());
					pstmt.setString(10, bean.getContent());
					pstmt.setTimestamp(11, bean.getSenddate());
					pstmt.setTimestamp(12, bean.getAccpetdate());
					pstmt.setString(13, bean.getAcceptstate());
					pstmt.setTimestamp(14, bean.getViewdate());
					pstmt.setString(15, bean.getViewstate());
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
			GroupacceptBean bean = (GroupacceptBean) obj;
			String statement = "UPDATE IM_GROUP_ACCEPT SET "
					+ "ACCEPT_SEQ_ID=?,GROUP_MSG_ID=?,MESSAGE_TYPE=?,FROM_ID=?,FROM_NAME=?,TO_GROUP_ID=?,TO_GROUP_NAME=?,"
					+ "TO_ID=?,TO_NAME=?,CONTENT=?,SEND_DATE=?,ACCPET_DATE=?,ACCEPT_STATE=?,VIEW_DATE=?,VIEW_STATE=?"
					+ " WHERE ACCEPT_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getAcceptseqid());
			pstmt.setString(2, bean.getGroupmsgid());
			pstmt.setString(3, bean.getMessagetype());
			pstmt.setString(4, bean.getFromid());
			pstmt.setString(5, bean.getFromname());
			pstmt.setString(6, bean.getTogroupid());
			pstmt.setString(7, bean.getTogroupname());
			pstmt.setString(8, bean.getToid());
			pstmt.setString(9, bean.getToname());
			pstmt.setString(10, bean.getContent());
			pstmt.setTimestamp(11, bean.getSenddate());
			pstmt.setTimestamp(12, bean.getAccpetdate());
			pstmt.setString(13, bean.getAcceptstate());
			pstmt.setTimestamp(14, bean.getViewdate());
			pstmt.setString(15, bean.getViewstate());
			pstmt.setBigDecimal(16, bean.getAcceptseqid());
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

	public void updateBatch(List<GroupacceptBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "UPDATE IM_GROUP_ACCEPT SET "
					+ "ACCEPT_SEQ_ID=?,GROUP_MSG_ID=?,MESSAGE_TYPE=?,FROM_ID=?,FROM_NAME=?,TO_GROUP_ID=?,"
					+ "TO_GROUP_NAME=?,TO_ID=?,TO_NAME=?,CONTENT=?,SEND_DATE=?,ACCPET_DATE=?,ACCEPT_STATE=?,"
					+ "VIEW_DATE=?,VIEW_STATE=?"
					+ " WHERE ACCEPT_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					GroupacceptBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getAcceptseqid());
					pstmt.setString(2, bean.getGroupmsgid());
					pstmt.setString(3, bean.getMessagetype());
					pstmt.setString(4, bean.getFromid());
					pstmt.setString(5, bean.getFromname());
					pstmt.setString(6, bean.getTogroupid());
					pstmt.setString(7, bean.getTogroupname());
					pstmt.setString(8, bean.getToid());
					pstmt.setString(9, bean.getToname());
					pstmt.setString(10, bean.getContent());
					pstmt.setTimestamp(11, bean.getSenddate());
					pstmt.setTimestamp(12, bean.getAccpetdate());
					pstmt.setString(13, bean.getAcceptstate());
					pstmt.setTimestamp(14, bean.getViewdate());
					pstmt.setString(15, bean.getViewstate());
					pstmt.setBigDecimal(16, bean.getAcceptseqid());
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
			GroupacceptBean bean = (GroupacceptBean) obj;
			String statement = "DELETE FROM IM_GROUP_ACCEPT "
					+ " WHERE ACCEPT_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getAcceptseqid());
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

	public void deleteBatch(List<GroupacceptBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "DELETE FROM  IM_GROUP_ACCEPT "
					+ " WHERE ACCEPT_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					GroupacceptBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getAcceptseqid());
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

	public List<GroupacceptBean> query() throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<GroupacceptBean> list = new ArrayList<GroupacceptBean>();
		try {
			String statement = "SELECT * FROM IM_GROUP_ACCEPT";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				GroupacceptBean bean = new GroupacceptBean();
				bean.setAcceptseqid(rs.getBigDecimal("ACCEPT_SEQ_ID"));
				bean.setGroupmsgid(rs.getString("GROUP_MSG_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setTogroupid(rs.getString("TO_GROUP_ID"));
				bean.setTogroupname(rs.getString("TO_GROUP_NAME"));
				bean.setToid(rs.getString("TO_ID"));
				bean.setToname(rs.getString("TO_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setAccpetdate(rs.getTimestamp("ACCPET_DATE"));
				bean.setAcceptstate(rs.getString("ACCEPT_STATE"));
				bean.setViewdate(rs.getTimestamp("VIEW_DATE"));
				bean.setViewstate(rs.getString("VIEW_STATE"));
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
			GroupacceptBean bean = (GroupacceptBean) obj;
			String statement = "SELECT * FROM IM_GROUP_ACCEPT "
					+ " WHERE ACCEPT_SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getAcceptseqid());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setAcceptseqid(rs.getBigDecimal("ACCEPT_SEQ_ID"));
				bean.setGroupmsgid(rs.getString("GROUP_MSG_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setTogroupid(rs.getString("TO_GROUP_ID"));
				bean.setTogroupname(rs.getString("TO_GROUP_NAME"));
				bean.setToid(rs.getString("TO_ID"));
				bean.setToname(rs.getString("TO_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setAccpetdate(rs.getTimestamp("ACCPET_DATE"));
				bean.setAcceptstate(rs.getString("ACCEPT_STATE"));
				bean.setViewdate(rs.getTimestamp("VIEW_DATE"));
				bean.setViewstate(rs.getString("VIEW_STATE"));
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

	public List<GroupacceptBean> queryByCause(String sql, Object... sqlParams)
			throws Exception {
		ArrayList<GroupacceptBean> list = new ArrayList<GroupacceptBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM IM_GROUP_ACCEPT WHERE 1=1 " + sql;
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
				BindParamUtils.bindPreparedObject(pstmt, i + 1, sqlParams[i]);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				GroupacceptBean bean = new GroupacceptBean();
				bean.setAcceptseqid(rs.getBigDecimal("ACCEPT_SEQ_ID"));
				bean.setGroupmsgid(rs.getString("GROUP_MSG_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setTogroupid(rs.getString("TO_GROUP_ID"));
				bean.setTogroupname(rs.getString("TO_GROUP_NAME"));
				bean.setToid(rs.getString("TO_ID"));
				bean.setToname(rs.getString("TO_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setAccpetdate(rs.getTimestamp("ACCPET_DATE"));
				bean.setAcceptstate(rs.getString("ACCEPT_STATE"));
				bean.setViewdate(rs.getTimestamp("VIEW_DATE"));
				bean.setViewstate(rs.getString("VIEW_STATE"));
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
