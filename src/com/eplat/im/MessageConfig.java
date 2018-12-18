package com.eplat.im;

import com.eplat.utils.RedisUtils;
import com.eplat.utils.SpringManager;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 添加回话管理
 * 
 * @author Administrator
 *
 */
public class MessageConfig {
	// 当前服务器
	public static String currentServer = "server1";
	public static ConcurrentHashMap<String, WebSocketSession> machineList = new ConcurrentHashMap<String, WebSocketSession>();
	public static ConcurrentHashMap<String, WebSocketSession> mobileList = new ConcurrentHashMap<String, WebSocketSession>();
	

	public static void addSession(String userid,String clienttype, WebSocketSession session)
			throws Exception {
		if ("pc".equalsIgnoreCase(clienttype)){//电脑端连接
			machineList.put(userid, session);
		} else {
			mobileList.put(userid, session);
		}
		addServerToRedis(userid,clienttype);		
	}

	public static void deleteSession(String userid,String clienttype) throws Exception {
		if("pc".equalsIgnoreCase(clienttype)){
			machineList.remove(userid);
		} else {
			mobileList.remove(userid);
		}
		removeRedis(userid,clienttype);
	}

	public static void sendToMQ(String serverFlag,String content) {
		AmqpTemplate amqpTemplate = (AmqpTemplate) SpringManager.getService("amqpTemplate");
		amqpTemplate.convertAndSend(serverFlag, content);
	}
	public static void sendGroupToMQ(String content) {
		AmqpTemplate amqpTemplate = (AmqpTemplate) SpringManager.getService("amqpTemplate");
		amqpTemplate.convertAndSend("group", content);
	}
	/**
	 * 设置影响的队列
	 * @param content
	 */
	public static void sendResponseToMQ(String serverFlag,String content) {
		AmqpTemplate amqpTemplate = (AmqpTemplate) SpringManager.getService("amqpTemplate");
		try {
			amqpTemplate.convertAndSend(serverFlag, content);
		} catch (AmqpException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 离线消息队列
	 * @param content
	 */
	public static void sendOffLineMsgToMQ(String content) {
		AmqpTemplate amqpTemplate = (AmqpTemplate) SpringManager.getService("amqpTemplate");
		amqpTemplate.convertAndSend("offlinetext", content);
	}


	/**
	 *撤回消息指令队列
	 * @param content
	 */
	public static void sendRevokeMsgToMQ(String serverFlag,String content) {
		AmqpTemplate amqpTemplate = (AmqpTemplate) SpringManager.getService("amqpTemplate");
		amqpTemplate.convertAndSend(serverFlag, content);
	}

	/**
	 * @param queueNameKey 队列绑定的key
	 * @param content 消息内容
	 * @param content
	 */
	public static void sendToMQByQueueName(String queueNameKey ,String content) {
		AmqpTemplate amqpTemplate = (AmqpTemplate) SpringManager.getService("amqpTemplate");
		amqpTemplate.convertAndSend(queueNameKey, content);
	}






	/**
	 * 获取所在服务器
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public static String getServer(String userid,String clienttype) throws Exception {
		return RedisUtils.getString("IM_"+userid+"_"+clienttype);
	}

	public static Map<String,WebSocketSession>  getSession(String userid) throws Exception {
		Map<String,WebSocketSession> data=new HashMap<String,WebSocketSession>();
		if (machineList.containsKey(userid)){			
			data.put("pc", machineList.get(userid));
		}
		if (mobileList.containsKey(userid)){
			data.put("mobile", machineList.get(userid));
		}
		return data;
	}
	/**
	 * 获取回话
	 * @param userid
	 * @param clienttype
	 * @return
	 * @throws Exception
	 */
	public static WebSocketSession  getSession(String userid,String clienttype) throws Exception {
		if ("pc".equalsIgnoreCase(clienttype)){
			return machineList.get(userid);
		}
		if ("mobile".equalsIgnoreCase(clienttype)){
			return mobileList.get(userid);
		} 
		return null;
	
	}
	private static void removeRedis(String userid,String clienttype) throws Exception {
		RedisUtils.delete("IM_"+userid+"_"+clienttype);
	}

	private static void addServerToRedis(String userid,String clienttype) throws Exception {
		RedisUtils.set("IM_"+userid+"_"+clienttype, currentServer, 10 * 60);	
	}

}
