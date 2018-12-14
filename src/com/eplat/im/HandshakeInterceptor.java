package com.eplat.im;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;
/**
 * 握手拦截，解决socketjs和websocket同时链接
 * @author Administrator
 *
 */
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	private Logger logger=Logger.getLogger(HandshakeInterceptor.class);
	@Override
    public boolean beforeHandshake(ServerHttpRequest request,
            ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        if (request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {

            request.getHeaders().set("Sec-WebSocket-Extensions",
                    "permessage-deflate");

        }
        logger.info("握手前");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception ex) {
    	logger.info("握手后");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
