package com.eplat.im;


import com.eplat.db.ConnectionManager;
import com.eplat.db.DBConnection;
import com.eplat.im.domain.GroupacceptBean;
import com.eplat.im.domain.GroupsendBean;
import com.eplat.im.domain.MessagesendBean;
import com.eplat.utils.BeanUtil;
import com.eplat.utils.DateTimeUtils;
import com.eplat.utils.RedisUtils;
import com.eplat.utils.StringUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


/**
 * 响应消息的处理
 * @author Administrator
 *
 */
public class ResponseTextListener {
	private Logger logger = Logger.getLogger(ResponseTextListener.class);
	
	public void receiveMessage(String msg) {
		//System.out.println("收到消息送达报告："+msg);
		//logger.info("收到消息回执报告："+msg);
		DBConnection connection = null;
		try {
			JSONObject jo = JSONObject.fromObject(msg);
			if (MessageType.TEXT_RESULT.equalsIgnoreCase(jo
					.getString("messagetype"))){//消息送达报告处理
				connection = ConnectionManager.getNoThreadConnection("default");

				this.responseTextMsg(connection,
						jo.getString("messageid"),jo.getString("clienttype"), DateTimeUtils
								.strToSqlLongDate(jo.getString("acceptdate")));				
			} else if (MessageType.GROUP_TXT_RESULT.equalsIgnoreCase(jo
					.getString("messagetype"))){//群组消息送达报告
				connection = ConnectionManager.getNoThreadConnection("default");
				this.responseGroupTextMsg(connection,
						jo.getString("fromid"),jo.getString("groupmsgid"),jo.getString("clienttype"), DateTimeUtils
								.strToSqlLongDate(jo.getString("acceptdate")),jo);
			} else if (MessageType.VIEW_TEXT.equalsIgnoreCase(jo
					.getString("messagetype"))){//消息查看报告
				logger.info("收到消息查看报告========================"+jo.toString());
				connection = ConnectionManager.getNoThreadConnection("default");
				this.responseViewMsg(connection, jo.getString("messageid"), jo.getString("clienttype"), DateTimeUtils
						.strToSqlLongDate(jo.getString("viewdate")));
			} else if (MessageType.GROUP_TXT_VIEW.equalsIgnoreCase(jo
					.getString("messagetype"))){//群组消息送达报告
				connection = ConnectionManager.getNoThreadConnection("default");
				this.responseGroupViewMsg(connection, jo.getString("fromid"),jo.getString("groupmsgid"),jo.getString("clienttype"), DateTimeUtils
						.strToSqlLongDate(jo.getString("viewdate")),jo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("消息反馈影响处理：" + e.getMessage());
		} finally {
			try {
				if (connection != null) {
					connection.closeConnection();
				}
			} catch (Exception e) {
				logger.error("消息反馈影响处理：" + e.getMessage());
			}
		}
	}
	
	/**
	 * 发送到其它客户端
	 * @param sendBean
	 * @throws Exception
	 */
	private void sendToClient(MessagesendBean sendBean,String clienttype) throws Exception{
		String serverFlag=MessageConfig.getServer(sendBean.getToid(), clienttype);
		JSONObject jo=new JSONObject();
		jo.put("messageid", sendBean.getMessageid());
		jo.put("clienttype", clienttype);
		jo.put("viewdate", DateTimeUtils.sqlDateToLongStr(sendBean.getViewdate()));
		jo.put("messagetype", MessageType.VIEW_TEXT_RESULT);
		jo.put("status", "0");
		jo.put("message", "OK");
		if (!StringUtils.hasLength(serverFlag)){//其他设备不在线，直接加入Redis缓存
			RedisUtils.setMap(sendBean.getToid() + "_"+clienttype, jo.getString("messageid"),
					jo.toString());
		} else {//设备在线，直接送到MQ中			
			MessageConfig.sendToMQ(serverFlag, jo.toString());
		}
	}
	/**
	 * 发送群组消息给其它客户端
	 * @param acceptBean
	 * @param clienttype
	 * @throws Exception
	 */
	private void sendToGroupClient(GroupacceptBean acceptBean,String clienttype) throws Exception{
		String serverFlag=MessageConfig.getServer(acceptBean.getToid(), clienttype);
		JSONObject jo=new JSONObject();
		jo.put("groupmsgid", acceptBean.getGroupmsgid());
		jo.put("clienttype", clienttype);
		jo.put("toid", acceptBean.getToid());
		jo.put("toname", acceptBean.getToname());
		jo.put("viewdate", DateTimeUtils.sqlDateToLongStr(acceptBean.getViewdate()));
		jo.put("messagetype", MessageType.GROUP_TXT_VIEW_RESULT);
		jo.put("status", "0");
		jo.put("message", "OK");
		if (!StringUtils.hasLength(serverFlag)){//其他设备不在线，直接加入Redis缓存
			RedisUtils.setMap(acceptBean.getToid() + "_"+clienttype, jo.getString("groupmsgid"),
					jo.toString());
		} else {//设备在线，直接送到MQ中			
			MessageConfig.sendToMQ(serverFlag, jo.toString());
		}
	}
	/**
	 * 群组响应消息------群组消息送达
	 * @param connection
	 * @param messageid
	 * @param clienttype
	 * @param acceptdate
	 * @throws Exception
	 */
	private void responseGroupTextMsg(DBConnection connection,
			String acceptid,String messageid,String clienttype, java.sql.Timestamp acceptdate,JSONObject groupRespMsg) throws Exception {
		try {

			String sendKey=messageid;
			String acceptKey=messageid+"_"+acceptid;
			String sendContent=RedisUtils.getString(sendKey);
			String acceptContent=RedisUtils.getString(acceptKey);
			int incAcceptNum=0;
			connection.beginTrans();
			if (StringUtils.hasLength(acceptContent)){
				JSONObject jo = JSONObject.fromObject(acceptContent);
				GroupacceptBean groupacceptBean=(GroupacceptBean)BeanUtil.jsonToBean(jo, GroupacceptBean.class);
				if (!"1".equalsIgnoreCase(groupacceptBean.getAcceptstate())){
					incAcceptNum++;
					groupacceptBean.setAcceptstate("1");
					groupacceptBean.setAccpetdate(acceptdate);
					RedisUtils.set(acceptKey, JSONObject.fromObject(groupacceptBean).toString());	
					String sql = "UPDATE IM_GROUP_ACCEPT SET ACCPET_DATE=?,ACCEPT_STATE='1' WHERE GROUP_MSG_ID=? AND TO_ID=?";
					connection.execute(sql, acceptdate, messageid,acceptid);
					
				}
			}
			if (StringUtils.hasLength(sendContent)){
				JSONObject jo = JSONObject.fromObject(sendContent);
				GroupsendBean sendBean = (GroupsendBean) BeanUtil.jsonToBean(
						jo, GroupsendBean.class);
				sendBean.setAcceptnum(sendBean.getAcceptnum()+incAcceptNum);
				//设置到缓存
				RedisUtils.set(sendKey, JSONObject.fromObject(sendBean).toString());		
				String sql = "UPDATE IM_GROUP_SEND SET ACCEPT_NUM=? WHERE GROUP_MSG_ID=?";
				connection.execute(sql, sendBean.getAcceptnum(),sendBean.getGroupmsgid());
			}
			connection.commit();
//===================================liulongyun add start===============================//
			//输出送达报告
			if(!groupRespMsg.get("fromid").equals(groupRespMsg.get("toid"))){
				WebSocketSession session=MessageConfig.getSession(groupRespMsg.getString("toid"),groupRespMsg.getString("clienttype"));
				if(session!=null){
					session.sendMessage(new TextMessage(groupRespMsg.toString()));
				}
			}
//===================================liulongyun add end===============================//

		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
			logger.error("放入Redis失败：" + e.getMessage());
		}
	}
	/**
	 * 文本消息反馈----单聊消息送达
	 *
	 * @param connection
	 * @param messageid
	 * @param acceptdate
	 * @throws Exception
	 */
	private void responseTextMsg(DBConnection connection,
			String messageid,String clienttype, java.sql.Timestamp acceptdate) throws Exception {
		try {			
			String redisKey="IM_MSG_" + messageid;
			String content=RedisUtils.getString(redisKey);
			if(content!=null){
				JSONObject jo = JSONObject.fromObject(content);
				MessagesendBean sendBean = (MessagesendBean) BeanUtil.jsonToBean(
						jo, MessagesendBean.class);
				sendBean.setAcceptstate("1");
				sendBean.setAccpetdate(acceptdate);
				connection.beginTrans();
				String sql = "UPDATE IM_MESSAGE_SEND SET ACCPET_DATE=?,ACCEPT_STATE='1' WHERE MESSAGE_ID=?";
				connection.execute(sql, acceptdate, messageid);
				sql = "UPDATE IM_MESSAGE_ACCEPT SET ACCPET_DATE=?,ACCEPT_STATE='1' WHERE MESSAGE_ID=? ";
				connection.execute(sql, acceptdate, messageid);
				connection.commit();
				jo.put("acceptstate", "1");
				jo.put("acceptdate", DateTimeUtils.sqlDateToLongStr(acceptdate));
				RedisUtils.set(redisKey, jo.toString());

//===================================liulongyun add start===============================//
				//输出送达报告
				JSONObject responMsg = new JSONObject();
				responMsg.put("groupmsgid",jo.get("groupmsgid"));
				responMsg.put("messagetype",MessageType.TEXT_RESULT);

				responMsg.put("fromid",jo.get("toid"));
				responMsg.put("fromname",jo.get("toname"));
				responMsg.put("toid",jo.get("fromid"));
				responMsg.put("toname",jo.get("fromname"));
				responMsg.put("senddate",DateTimeUtils.getCurrentLongDate());
				responMsg.put("acceptdate",jo.get("acceptdate"));
				responMsg.put("clienttype",jo.get("clienttype"));
				responMsg.put("server",MessageConfig.currentServer);

				WebSocketSession session=MessageConfig.getSession(responMsg.getString("toid"),responMsg.getString("clienttype"));
				if(session!=null){
					session.sendMessage(new TextMessage(responMsg.toString()));
				}
//===================================liulongyun add end===============================//


			}



		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
			logger.error("放入Redis失败：" + e.getMessage());
		}
	}
	/**
	 * 相应查看报告----单聊消息查看
	 * @param connection
	 * @param messageid
	 * @param clienttype
	 * @param viewdate
	 * @throws Exception
	 */
	private void responseViewMsg(DBConnection connection,
			String messageid,String clienttype, java.sql.Timestamp viewdate) throws Exception {
		try {
			logger.info("设置消息为已读messageid========================"+messageid);
			String redisKey="IM_MSG_" + messageid;
			String content=RedisUtils.getString(redisKey);
			if(content!=null){
				JSONObject jo = JSONObject.fromObject(content);
				MessagesendBean sendBean = (MessagesendBean) BeanUtil.jsonToBean(
						jo, MessagesendBean.class);

				sendBean.setViewdate(viewdate);
				sendBean.setViewstate("1");
				connection.beginTrans();
				String sql = "UPDATE IM_MESSAGE_SEND SET VIEW_DATE=?,VIEW_STATE='1' WHERE MESSAGE_ID=?";
				String sqlaccept = "UPDATE IM_MESSAGE_ACCEPT SET VIEW_DATE=?,VIEW_STATE='1' WHERE MESSAGE_ID=?";

				connection.execute(sql, viewdate,messageid);
				connection.execute(sqlaccept, viewdate,messageid);
				connection.commit();

				logger.info("设置消息为已读成功messageid========================"+messageid);
				RedisUtils.delete(redisKey);
				if ("pc".equalsIgnoreCase(clienttype)){
					this.sendToClient(sendBean, "mobile");
				} else if ("mobile".equalsIgnoreCase(clienttype)){
					this.sendToClient(sendBean, "pc");
				}

				//发送消息已查看报告到对方


//===================================liulongyun add start===============================//


				//输出查看报告todo
				JSONObject responMsg = new JSONObject();
				responMsg.put("messageid",jo.get("messageid"));
				responMsg.put("messagetype",MessageType.VIEW_TEXT_RESULT);//查看消息报告

				responMsg.put("fromid",jo.get("toid"));
				responMsg.put("fromname",jo.get("toname"));
				responMsg.put("toid",jo.get("fromid"));
				responMsg.put("toname",jo.get("fromname"));
				responMsg.put("senddate",DateTimeUtils.getCurrentLongDate());
				responMsg.put("viewdate", DateTimeUtils.dateToLongStr(viewdate));
				responMsg.put("clienttype",jo.get("clienttype"));
				responMsg.put("server",MessageConfig.currentServer);


				WebSocketSession session=MessageConfig.getSession(responMsg.getString("toid"),responMsg.getString("clienttype"));

				if(session!=null){
					logger.info("发送的已查看回执："+responMsg.toString());
					session.sendMessage(new TextMessage(responMsg.toString()));
				}
//===================================liulongyun add end===============================//
			}






		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
			logger.error("放入Redis失败：" + e.getMessage());
		}
	}
	/**
	 * 用户组消息查看
	 * @param connection
	 * @param acceptid
	 * @param messageid
	 * @param clienttype
	 * @param viewdate
	 * @throws Exception
	 */
	private void responseGroupViewMsg(DBConnection connection,String acceptid,
			String messageid,String clienttype, java.sql.Timestamp viewdate,JSONObject groupViewMsg) throws Exception {
		try {			
			String sendKey=messageid;
			String acceptKey=messageid+"_"+acceptid;
			String sendContent=RedisUtils.getString(sendKey);
			String acceptContent=RedisUtils.getString(acceptKey);
			int incViewNum=0;
			GroupacceptBean groupacceptBean=null;
			connection.beginTrans();
			if (StringUtils.hasLength(acceptContent)){
				JSONObject jo = JSONObject.fromObject(acceptContent);
				groupacceptBean=(GroupacceptBean)BeanUtil.jsonToBean(jo, GroupacceptBean.class);
				if (!"1".equalsIgnoreCase(groupacceptBean.getViewstate())){
					incViewNum++;
					groupacceptBean.setViewstate("1");
					groupacceptBean.setViewdate(viewdate);
					RedisUtils.set(acceptKey, JSONObject.fromObject(groupacceptBean).toString());	
					String sql = "UPDATE IM_GROUP_ACCEPT SET VIEW_DATE=?,VIEW_STATE='1' WHERE GROUP_MSG_ID=? AND TO_ID=?";
					connection.execute(sql, viewdate, messageid,acceptid);
					if ("pc".equalsIgnoreCase(clienttype)){
						this.sendToGroupClient(groupacceptBean, "mobile");
					} else if ("mobile".equalsIgnoreCase(clienttype)){
						this.sendToGroupClient(groupacceptBean, "pc");
					}
				}
			}
			if (StringUtils.hasLength(sendContent)){
				JSONObject jo = JSONObject.fromObject(sendContent);
				GroupsendBean sendBean = (GroupsendBean) BeanUtil.jsonToBean(
						jo, GroupsendBean.class);
				sendBean.setViewnum(sendBean.getViewnum()+incViewNum);
				//设置到缓存
				RedisUtils.set(sendKey, JSONObject.fromObject(sendBean).toString());		
				String sql = "UPDATE IM_GROUP_SEND SET VIEW_NUM=? WHERE GROUP_MSG_ID=?";
				connection.execute(sql, sendBean.getViewnum(),sendBean.getGroupmsgid());
			}
			connection.commit();
			//发送消息已查看报告到对方
//===================================liulongyun add start===============================//

			if(!groupViewMsg.get("fromid").equals(groupViewMsg.get("toid"))){
				WebSocketSession session=MessageConfig.getSession(groupViewMsg.getString("toid"),groupViewMsg.getString("clienttype"));
				groupViewMsg.put("messagetype", MessageType.GROUP_TXT_VIEW_RESULT);
				if(session!=null){
					session.sendMessage(new TextMessage(groupViewMsg.toString()));
				}
			}


//===================================liulongyun add end===============================//

		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
			logger.error("放入Redis失败：" + e.getMessage());
		}


	}
	
}
