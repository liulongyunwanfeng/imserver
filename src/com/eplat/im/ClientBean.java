package com.eplat.im;

import org.springframework.web.socket.WebSocketSession;

public class ClientBean {
	// websocket连接客户端
	private WebSocketSession session;
	// 最后次访问时间
	private long lastAccessTime;

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

}
