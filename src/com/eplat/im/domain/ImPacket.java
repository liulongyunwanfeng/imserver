package com.eplat.im.domain;

import com.eplat.annotation.EntityBean;

/**
 * 传输消息报文
 * @author Administrator
 *
 */
public class ImPacket extends  EntityBean{
	private String messageType;
	private int status;
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
