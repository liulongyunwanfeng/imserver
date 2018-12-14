package com.eplat.im;

import com.eplat.db.ConnectionManager;
import com.eplat.db.DBConnection;
import com.eplat.im.dao.MessageacceptDao;
import com.eplat.im.dao.MessagesendDao;
import com.eplat.im.domain.MessagesendBean;
import com.eplat.utils.DateTimeUtils;
import com.eplat.utils.RedisUtils;
import com.eplat.utils.SpringManager;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 消息接收处理-----消费者
 * 
 * @author Administrator
 *
 */
public class MessageListener {
	private Logger logger = Logger.getLogger(MessageListener.class);

	public void receiveMessage(String msg) {

		System.out.println("消息监听收到消息：==========="+msg);


		DBConnection connection = null;
		try {
			JSONObject jo = JSONObject.fromObject(msg);
			if (MessageType.LOGIN_OUT.equalsIgnoreCase(jo
					.getString("messagetype"))) { // 登出
				MessageConfig.deleteSession(jo.getString("userid"),
						jo.getString("clienttype"));
			} else if (MessageType.GROUP_TXT_USER.equalsIgnoreCase(jo.getString("messagetype"))){//群组接收用户消息
				WebSocketSession session=MessageConfig.getSession(jo.getString("toid"),jo.getString("clienttype"));	
				//写入库
				if (session==null){
					RedisUtils.setMap(jo.getString("toid")+"_"+jo.getString("clienttype"), jo.getString("groupmsgid"), msg);
				} else {
					jo.put("status", "0");
					jo.put("message", "OK");
					session.sendMessage(new TextMessage(jo.toString()));
				}
			} else if (MessageType.GROUP_TXT_VIEW_RESULT.equalsIgnoreCase(jo.getString("messagetype"))){//发送给其他客户端的群组消息
				WebSocketSession session=MessageConfig.getSession(jo.getString("toid"),jo.getString("clienttype"));	
				//写入库
				if (session==null){
					RedisUtils.setMap(jo.getString("toid")+"_"+jo.getString("clienttype"), jo.getString("groupmsgid"), msg);
				} else {
					jo.put("status", "0");
					jo.put("message", "OK");
					session.sendMessage(new TextMessage(jo.toString()));
				}
			}  else if (MessageType.TEXT.equalsIgnoreCase(jo
					.getString("messagetype"))) {// 文本消息
				System.out.println("接收消息处理-MQ订阅");
				System.out.println("接受者ID=" + jo.getString("toid"));

				WebSocketSession session = MessageConfig.getSession(jo
						.getString("toid"),jo.getString("clienttype"));

				MessagesendBean send = new MessagesendBean();
				send.setSeqid(new BigDecimal(UUID.randomUUID()
						.getMostSignificantBits()));
				send.setMessageid(jo.getString("messageid"));
				send.setMessagetype(jo.getString("messagetype"));
				send.setFromid(jo.getString("fromid"));
				send.setFromname(jo.getString("fromname"));
				send.setToid(jo.getString("toid"));
				send.setToname(jo.getString("toname"));
				send.setContent(jo.getString("content"));
				send.setSenddate(DateTimeUtils.strToSqlLongDate(jo
						.getString("senddate")));
				send.setClienttype(jo.getString("clienttype"));
				send.setAcceptstate("0");
				send.setViewstate("0");


				System.out.println("发送消息对象=====："+JSONObject.fromObject(send).toString());

				JSONObject contentJsonObj= JSONObject.fromObject(jo.getString("content"));
				send.setBusinesstype(contentJsonObj.getString("businessType"));//liulongyun扩展

				connection = ConnectionManager.getNoThreadConnection("default");
				this.writeMessageToRedis(connection, send);


				if (session != null) {// 接受者在线
					System.out.println("接受者在线");
					System.out.println("发送消息对象=====："+JSONObject.fromObject(send).toString());
					JSONObject result = JSONObject.fromObject(send);
					result.put("status", "0");
					result.put("message", "OK");
					session.sendMessage(new TextMessage(jo.toString()));

				} else {//如果用户不在线，直接放入缓存
					RedisUtils.setMap(send.getToid()+"_"+jo.getString("clienttype"), jo.getString("messageid"), msg);
					System.out.println("接受者离线");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订阅消息处理：" + e.getMessage());
		} finally {
			try {
				if (connection != null) {
					connection.closeConnection();
				}
			} catch (Exception e) {
				logger.error("订阅消息处理：" + e.getMessage());
			}
		}
	}

	
	/**
	 * 写入消息
	 * 
	 * @param send
	 */
	private void writeMessageToRedis(DBConnection connection,
			MessagesendBean send) throws Exception {
		try {
			RedisUtils.set("IM_MSG_" + send.getMessageid(), JSONObject
					.fromObject(send).toString());

			RedisUtils.setMap("IM_" + send.getToid(), send.getMessageid(),
					send.getFromid());


			connection.beginTrans();
			MessageacceptDao messageacceptDao = (MessageacceptDao) SpringManager.getService("messageacceptDao");
			messageacceptDao.setDBConnection(connection);
			messageacceptDao.insert(send);
			MessagesendDao messagesendDao = (MessagesendDao) SpringManager.getService("messagesendDao");
			messagesendDao.setDBConnection(connection);
			messagesendDao.insert(send);
			connection.commit();


		} catch (Exception e) {
			connection.rollback();
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("放入Redis失败：" + e.getMessage());
		}
	}
}
