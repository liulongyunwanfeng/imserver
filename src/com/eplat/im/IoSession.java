package com.eplat.im;

import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class IoSession {

	public String userid;
	public Map<String,ClientBean> clientList=new HashMap<String,ClientBean>();
	
	public void sendMessage(String message) throws Exception {
		if (clientList!=null&&clientList.size()>0){
			Iterator<Entry<String,ClientBean>> clientit=clientList.entrySet().iterator();
			while(clientit.hasNext()){
				Entry<String,ClientBean> oneClient=clientit.next();
				ClientBean client=oneClient.getValue();
				System.out.println("发送消息---IOSEssion");
				if (client!=null&&System.currentTimeMillis()- client.getLastAccessTime()<=MessageType.MAX_TIME){
					client.getSession().sendMessage(new TextMessage(message));
				} else {//放入Redis缓存
					System.out.println("发送消息---无效消息");
				}				
			}
		} 
		
	}

}
