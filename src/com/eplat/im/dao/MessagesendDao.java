package com.eplat.im.dao;

import com.eplat.db.BindParamUtils;
import com.eplat.db.dao.AbstractBaseDao;
import com.eplat.db.dao.BaseDao;
import com.eplat.im.domain.MessagesendBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @类名称：MessagesendDao
 * @类描述：消息记录
 * @创建人：代码生成器
 * @创建时间：2017年09月22日 22:24:58
 */
public class MessagesendDao extends AbstractBaseDao implements BaseDao {
	/**
	 * 
	 * insert
	 * 
	 * @描述：新增数据
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public void insert(Object obj) throws Exception {
		PreparedStatement pstmt = null;
		try {
			MessagesendBean bean = (MessagesendBean) obj;
			String statement = "INSERT INTO IM_MESSAGE_SEND "
					+ "(SEQ_ID,MESSAGE_ID,MESSAGE_TYPE,FROM_ID,FROM_NAME,TO_ID,TO_NAME,CONTENT,SEND_DATE,ACCPET_DATE,ACCEPT_STATE,VIEW_DATE,VIEW_STATE,BUSINESS_TYPE)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getSeqid());
			pstmt.setString(2, bean.getMessageid());
			pstmt.setString(3, bean.getMessagetype());
			pstmt.setString(4, bean.getFromid());
			pstmt.setString(5, bean.getFromname());
			pstmt.setString(6, bean.getToid());
			pstmt.setString(7, bean.getToname());
			pstmt.setString(8, bean.getContent());
			pstmt.setTimestamp(9, bean.getSenddate());
			pstmt.setTimestamp(10, bean.getAccpetdate());
			pstmt.setString(11, bean.getAcceptstate());
			pstmt.setTimestamp(12, bean.getViewdate());
			pstmt.setString(13, bean.getViewstate());
			pstmt.setString(14, bean.getBusinesstype());
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
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void insertBatch(List<MessagesendBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "INSERT INTO IM_MESSAGE_SEND "
					+ "(SEQ_ID,MESSAGE_ID,MESSAGE_TYPE,FROM_ID,FROM_NAME,TO_ID,TO_NAME,CONTENT,SEND_DATE,ACCPET_DATE,ACCEPT_STATE,VIEW_DATE,VIEW_STATE,BUSINESS_TYPE)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					MessagesendBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getSeqid());
					pstmt.setString(2, bean.getMessageid());
					pstmt.setString(3, bean.getMessagetype());
					pstmt.setString(4, bean.getFromid());
					pstmt.setString(5, bean.getFromname());
					pstmt.setString(6, bean.getToid());
					pstmt.setString(7, bean.getToname());
					pstmt.setString(8, bean.getContent());
					pstmt.setTimestamp(9, bean.getSenddate());
					pstmt.setTimestamp(10, bean.getAccpetdate());
					pstmt.setString(11, bean.getAcceptstate());
					pstmt.setTimestamp(12, bean.getViewdate());
					pstmt.setString(13, bean.getViewstate());
					pstmt.setString(14, bean.getBusinesstype());
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
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void update(Object obj) throws Exception {
		PreparedStatement pstmt = null;
		try {
			MessagesendBean bean = (MessagesendBean) obj;
			String statement = "UPDATE IM_MESSAGE_SEND SET "
					+ "SEQ_ID=?,MESSAGE_ID=?,MESSAGE_TYPE=?,FROM_ID=?,FROM_NAME=?,TO_ID=?,TO_NAME=?,CONTENT=?,SEND_DATE=?,ACCPET_DATE=?,"
					+ "ACCEPT_STATE=?,VIEW_DATE=?,VIEW_STATE=?,BUSINESS_TYPE=?"
					+ " WHERE SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getSeqid());
			pstmt.setString(2, bean.getMessageid());
			pstmt.setString(3, bean.getMessagetype());
			pstmt.setString(4, bean.getFromid());
			pstmt.setString(5, bean.getFromname());
			pstmt.setString(6, bean.getToid());
			pstmt.setString(7, bean.getToname());
			pstmt.setString(8, bean.getContent());
			pstmt.setTimestamp(9, bean.getSenddate());
			pstmt.setTimestamp(10, bean.getAccpetdate());
			pstmt.setString(11, bean.getAcceptstate());
			pstmt.setTimestamp(12, bean.getViewdate());
			pstmt.setString(13, bean.getViewstate());
			pstmt.setString(14, bean.getBusinesstype());

			pstmt.setBigDecimal(15, bean.getSeqid());
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
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void updateBatch(List<MessagesendBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "UPDATE IM_MESSAGE_SEND SET "
					+ "SEQ_ID=?,MESSAGE_ID=?,MESSAGE_TYPE=?,FROM_ID=?,FROM_NAME=?,TO_ID=?,TO_NAME=?,CONTENT=?,SEND_DATE=?,ACCPET_DATE=?,"
					+ "ACCEPT_STATE=?,VIEW_DATE=?,VIEW_STATE=?,BUSINESS_TYPE=?"
					+ " WHERE SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					MessagesendBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getSeqid());
					pstmt.setString(2, bean.getMessageid());
					pstmt.setString(3, bean.getMessagetype());
					pstmt.setString(4, bean.getFromid());
					pstmt.setString(5, bean.getFromname());
					pstmt.setString(6, bean.getToid());
					pstmt.setString(7, bean.getToname());
					pstmt.setString(8, bean.getContent());
					pstmt.setTimestamp(9, bean.getSenddate());
					pstmt.setTimestamp(10, bean.getAccpetdate());
					pstmt.setString(11, bean.getAcceptstate());
					pstmt.setTimestamp(12, bean.getViewdate());
					pstmt.setString(13, bean.getViewstate());
					pstmt.setString(14, bean.getBusinesstype());


					pstmt.setBigDecimal(15, bean.getSeqid());
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
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void delete(Object obj) throws Exception {
		PreparedStatement pstmt = null;
		try {
			MessagesendBean bean = (MessagesendBean) obj;
			String statement = "DELETE FROM IM_MESSAGE_SEND "
					+ " WHERE SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getSeqid());
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
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public void deleteBatch(List<MessagesendBean> list) throws Exception {
		PreparedStatement pstmt = null;
		try {
			String statement = "DELETE FROM  IM_MESSAGE_SEND "
					+ " WHERE SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			if ((list != null) && (list.size() > 0)) {
				for (int i = 0; i < list.size(); i++) {
					MessagesendBean bean = list.get(i);
					pstmt.setBigDecimal(1, bean.getSeqid());
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
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public List<MessagesendBean> query() throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<MessagesendBean> list = new ArrayList<MessagesendBean>();
		try {
			String statement = "SELECT * FROM IM_MESSAGE_SEND";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MessagesendBean bean = new MessagesendBean();
				bean.setSeqid(rs.getBigDecimal("SEQ_ID"));
				bean.setMessageid(rs.getString("MESSAGE_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setToid(rs.getString("TO_ID"));
				bean.setToname(rs.getString("TO_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setAccpetdate(rs.getTimestamp("ACCPET_DATE"));
				bean.setAcceptstate(rs.getString("ACCEPT_STATE"));
				bean.setViewdate(rs.getTimestamp("VIEW_DATE"));
				bean.setViewstate(rs.getString("VIEW_STATE"));
				bean.setBusinesstype(rs.getString("BUSINESS_TYPE"));
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
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public Object queryPK(Object obj) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			MessagesendBean bean = (MessagesendBean) obj;
			String statement = "SELECT * FROM IM_MESSAGE_SEND "
					+ " WHERE SEQ_ID=?";
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			pstmt.setBigDecimal(1, bean.getSeqid());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setSeqid(rs.getBigDecimal("SEQ_ID"));
				bean.setMessageid(rs.getString("MESSAGE_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setToid(rs.getString("TO_ID"));
				bean.setToname(rs.getString("TO_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setAccpetdate(rs.getTimestamp("ACCPET_DATE"));
				bean.setAcceptstate(rs.getString("ACCEPT_STATE"));
				bean.setViewdate(rs.getTimestamp("VIEW_DATE"));
				bean.setViewstate(rs.getString("VIEW_STATE"));
				bean.setBusinesstype(rs.getString("BUSINESS_TYPE"));
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
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */

	public List<MessagesendBean> queryByCause(String sql, Object... sqlParams)
			throws Exception {
		ArrayList<MessagesendBean> list = new ArrayList<MessagesendBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String statement = "SELECT * FROM IM_MESSAGE_SEND WHERE 1=1 " + sql;
			pstmt = this.getDBConnection().createPreparedStatement(statement);
			for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
				BindParamUtils.bindPreparedObject(pstmt, i + 1, sqlParams[i]);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MessagesendBean bean = new MessagesendBean();
				bean.setSeqid(rs.getBigDecimal("SEQ_ID"));
				bean.setMessageid(rs.getString("MESSAGE_ID"));
				bean.setMessagetype(rs.getString("MESSAGE_TYPE"));
				bean.setFromid(rs.getString("FROM_ID"));
				bean.setFromname(rs.getString("FROM_NAME"));
				bean.setToid(rs.getString("TO_ID"));
				bean.setToname(rs.getString("TO_NAME"));
				bean.setContent(rs.getString("CONTENT"));
				bean.setSenddate(rs.getTimestamp("SEND_DATE"));
				bean.setAccpetdate(rs.getTimestamp("ACCPET_DATE"));
				bean.setAcceptstate(rs.getString("ACCEPT_STATE"));
				bean.setViewdate(rs.getTimestamp("VIEW_DATE"));
				bean.setViewstate(rs.getString("VIEW_STATE"));
				bean.setBusinesstype(rs.getString("BUSINESS_TYPE"));
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
