package com.eplat.im;

import com.eplat.db.ConnectionManager;
import com.eplat.im.dao.UserHeadImgDao;
import com.eplat.utils.DateTimeUtils;
import com.eplat.utils.RedisUtils;
import com.eplat.utils.SpringManager;
import com.eplat.utils.StringUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * websocket拦截句柄
 *
 *
 *
 *
 * 
 * @author Administrator
 *
 */
public class WebsocketHandler extends TextWebSocketHandler {
	private Logger logger = Logger.getLogger(WebsocketHandler.class);


	/**
	 * 连接关闭
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession wss, CloseStatus arg1)
			throws Exception {

		logger.info("连接关闭--" + DateTimeUtils.getCurrentLongDate());
		logger.info("连接关闭--arg1.getCode()==========CloseStatus.getReason()" + arg1.getCode()+"==========="+arg1.getReason());
		System.out.println("连接关闭--" + DateTimeUtils.getCurrentLongDate());

		try {
			if (MessageConfig.machineList.containsValue(wss)) {
				Iterator<Entry<String, WebSocketSession>> it = MessageConfig.machineList.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, WebSocketSession> data = it.next();
					if (data.getValue().equals(wss)) {
						MessageConfig.machineList.remove(data.getKey());
						MessageConfig.deleteSession(data.getKey(), "pc");
					}
				}
			}
			if (MessageConfig.mobileList.containsValue(wss)) {
				Iterator<Entry<String, WebSocketSession>> it = MessageConfig.mobileList.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, WebSocketSession> data = it.next();
					if (data.getValue().equals(wss)) {
						MessageConfig.mobileList.remove(data.getKey());
						MessageConfig.deleteSession(data.getKey(), "mobile");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}

	}

	/**
	 * 连接成功
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		logger.debug("连接成功--" + DateTimeUtils.getCurrentLongDate());
		JSONObject jo = new JSONObject();
		jo.put("status", "0");
		jo.put("messagetype", MessageType.CONNECTED);
		jo.put("server", MessageConfig.currentServer);
		jo.put("message", "OK");
		session.sendMessage(new TextMessage(jo.toString()));
	}

	/**
	 * 接收消息
	 */
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage wsm)
			throws Exception {

		//logger.info("websocket收到消息：================"+JSONObject.fromObject(wsm).toString());

		if (wsm.getPayloadLength() == 0) {
			logger.debug("接收消息为空：");
			JSONObject resultmsg = new JSONObject();
			resultmsg.put("status", "-1");
			resultmsg.put("messagetype", MessageType.ERROR_MSG);
			resultmsg.put("server", MessageConfig.currentServer);
			resultmsg.put("message", "消息不能为空");
			session.sendMessage(new TextMessage(resultmsg.toString()));

			return;
		}
		String messageContent = String.valueOf(wsm.getPayload());
		logger.info("接收到客户端消息:" + messageContent);


		JSONObject jo = null;
		String fromclienttype = null;//这个消息是从哪个端发来的
		try {
			jo = JSONObject.fromObject(messageContent);
			fromclienttype = jo.getString("clienttype");

		} catch (Exception ex) {
			logger.error("消息异常，不是有效的JSON格式:" + messageContent);
			JSONObject jo1 = new JSONObject();
			jo1.put("status", "-1");
			jo1.put("messagetype", MessageType.ERROR_MSG);
			jo1.put("server", MessageConfig.currentServer);
			jo1.put("message", "消息格式失败");
			session.sendMessage(new TextMessage(jo1.toString()));
		}


		jo.put("server",MessageConfig.currentServer);//为标识这条信息经过那个服务器

		// 心跳检测
		if (MessageType.HEART_BEAT.equalsIgnoreCase(jo.getString("messagetype"))) {
			// 心跳就不加队列，直接返回
			String clienttype = jo.getString("clienttype");
			String userid = jo.getString("userid");

			if(clienttype==null||userid==null){
				JSONObject jo1 = new JSONObject();
				jo1.put("status", "-1");
				jo1.put("messagetype", MessageType.ERROR_MSG);
				jo1.put("server", MessageConfig.currentServer);
				jo1.put("message", "userid和clienttype不能为空");
				session.sendMessage(new TextMessage(jo1.toString()));
				return;
			}


			JSONObject jo1 = new JSONObject();
			jo1.put("status", "0");
			jo1.put("messagetype", MessageType.HEART_BEAT_RESULT);
			jo1.put("server", MessageConfig.currentServer);
			jo1.put("message", "OK");

			Map<String, String> dataList = RedisUtils.getMapAll(userid+ "_" + clienttype);//这是读离线消息

			List<String> jsonList = new ArrayList<String>();
			Iterator<Entry<String, String>> it = dataList.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> data = it.next();
				jsonList.add(data.getValue());
			}
			if (jsonList != null && jsonList.size() > 0) {
				jo1.put("content", jsonList);
			} else {
				jo1.put("content", "-1");
			}
			MessageConfig.addSession(jo.getString("userid"),
					jo.getString("clienttype"), session);
			System.out.println("心跳相应返回离线消息:"+jo1.toString());
			session.sendMessage(new TextMessage(jo1.toString()));
			if(!jo.toString().contains("imsclientserver")){
				logger.info("发出心跳相应："+jo1.toString());
			}

			RedisUtils.delete(jo.getString("userid") + "_" + clienttype);

		} else if (MessageType.LOGIN_OUT.equalsIgnoreCase(jo.getString("messagetype"))) {// 退出
			logout(jo, session);

		} else if (MessageType.LOGIN_NOPWD.equalsIgnoreCase(jo
				.getString("messagetype"))) {// 直接登录，不需要密码
			JSONObject jo1 = new JSONObject();
			if(!StringUtils.hasLength(jo.getString("userid"))){
				jo1.put("status", CommonConstants.OPTION_RESULT_FAIL);
				jo1.put("messagetype", MessageType.LOGIN_RESULT);
				jo1.put("server", MessageConfig.currentServer);
				jo1.put("message", "userid不能为空");

			}else{
				jo1.put("status", "0");
				jo1.put("messagetype", MessageType.LOGIN_RESULT);
				jo1.put("server", MessageConfig.currentServer);
				jo1.put("message", "OK");
				MessageConfig.addSession(jo.getString("userid"),
						jo.getString("clienttype"), session);
			}

			session.sendMessage(new TextMessage(jo1.toString()));
			MessageConfig.addSession(jo.getString("userid"),jo.getString("clienttype"), session);



		} else if (MessageType.LOGIN_PWD.equalsIgnoreCase(jo.getString("messagetype"))) {// 带用户名密码登录
			JSONObject result = null;
			try {
				result = login(jo, session);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if("0".equals(result.get("status"))){
				//登陆成功
				MessageConfig.addSession(jo.getString("userid"),jo.getString("clienttype"), session);//保存socekt
			}
			session.sendMessage(new TextMessage(result.toString()));
		} else if (MessageType.INIT_MSG.equalsIgnoreCase(jo.getString("messagetype"))) {// 初始化加载消息-----未读消息
			String clienttype = jo.getString("clienttype");
			String userid = jo.getString("userid");

			if(clienttype==null||userid==null){
				JSONObject jo1 = new JSONObject();
				jo1.put("status", CommonConstants.OPTION_RESULT_FAIL);
				jo1.put("messagetype", MessageType.INIT_MSG_RESULT);
				jo1.put("server", MessageConfig.currentServer);
				jo1.put("message", "userid和clienttype不能为空");
				session.sendMessage(new TextMessage(jo1.toString()));
				return;
			}

			JSONObject jo1 = new JSONObject();
			jo1.put("messagetype", MessageType.INIT_MSG_RESULT);

			if(userid==null||clienttype==null){
				jo1.put("status", CommonConstants.OPTION_RESULT_FAIL);
				session.sendMessage(new TextMessage(jo1.toString()));
			}else{
				jo1.put("status", CommonConstants.OPTION_RESULT_SUCCESS);
				Map<String, String> dataList = RedisUtils.getMapAll(userid + "_" + clienttype);//读取缓存的离线消息
				List<String> jsonList = new ArrayList<String>();
				Iterator<Entry<String, String>> it = dataList.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, String> data = it.next();
					jsonList.add(data.getValue());
				}
				if (jsonList != null && jsonList.size() > 0) {
					jo1.put("content", jsonList);
				} else {
					jo1.put("content", "-1");
				}
				jo1.put("content", jsonList);
				session.sendMessage(new TextMessage(jo1.toString()));
				System.out.println("获取离线消息:"+jo1.toString());
				RedisUtils.delete(jo.getString("userid") + "_" + clienttype);//情况对应的离线消息
			}

		} else if (MessageType.GROUP_TXT.equalsIgnoreCase(jo.getString("messagetype"))) {// 群组消息
			try {
                logger.debug("进入发送群消息发送逻辑=================获取发送人头像");
                JSONObject content = (JSONObject) jo.get("content");
                String businessType = (String) content.get("businessType");
                if(businessType!=null&&(businessType.equals(CommonConstants.IM_BUSINESSTYPE_POINTTOPOINT)||businessType.equals(CommonConstants.IM_BUSINESSTYPE_GROUPPOINTTOPOINT))) {

                    Object headImgOwnerIdObj = jo.get("fromid");
                    Long headImgOwnerIdLong = null;
                    if (headImgOwnerIdObj instanceof String) {
                        headImgOwnerIdLong = new Long((String) headImgOwnerIdObj);
                    } else {
                        headImgOwnerIdLong = (Long) headImgOwnerIdObj;
                    }
                    String redisCatchUserHeadImgAndImstatus = RedisUtils.getString(CommonConstants.IM_MSG_FROMUSER_HEADIMGANDIMSTATUS + headImgOwnerIdLong);
                    if(null!=redisCatchUserHeadImgAndImstatus){
                        jo.put("imheadimg", JSONObject.fromObject(redisCatchUserHeadImgAndImstatus).get("imheadimg"));
                        jo.put("imstatus", JSONObject.fromObject(redisCatchUserHeadImgAndImstatus).get("imstatus"));

                    }else{
                        //如果是聊天消息"businessType": "pointtopoint"或"businessType": "grouppointtopoint"，设置发送人消息头像
                        UserHeadImgDao userHeadImgDao = (UserHeadImgDao) SpringManager.getService("userHeadImgDao");
                        userHeadImgDao.setDBConnection(ConnectionManager.getNoThreadConnection("default"));
						Map<String,String> fromUserHeadImgAndImStatusInfo = userHeadImgDao.getHeadImgByUserId(headImgOwnerIdLong);
						String fromUserHeadImgAndImStatusInfoJsonStr = JSONObject.fromObject(fromUserHeadImgAndImStatusInfo).toString();
						//设置头像缓存
						RedisUtils.set(CommonConstants.IM_MSG_FROMUSER_HEADIMGANDIMSTATUS+headImgOwnerIdLong,fromUserHeadImgAndImStatusInfoJsonStr,CommonConstants.IM_USERHEADIMG_CATCH_TIMEOUT);
                        jo.put("imheadimg", fromUserHeadImgAndImStatusInfo.get("imheadimg"));
                        jo.put("imstatus", fromUserHeadImgAndImStatusInfo.get("imstatus"));

                    }

                }

			} catch (Exception e) {
				jo.put("imheadimg", CommonConstants.getDefautHeadImg());
				jo.put("imstatus", CommonConstants.IM_STATUS_WORKING);
				e.printStackTrace();
			}
            logger.info("进入发送群消息发送逻辑=================获取发送人头像成功");

			try {
				MessageConfig.sendGroupToMQ(jo.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			logger.info("================放入mq成功开始写回执=================");
			//给个消息已发送回执------add by lly
			JSONObject mesHasSendInfo = new JSONObject();
			mesHasSendInfo.put("messageid",jo.getString("messageid"));
			mesHasSendInfo.put("status", CommonConstants.OPTION_RESULT_SUCCESS);
			mesHasSendInfo.put("messagetype", MessageType.GROUP_TEXT_HAS_SEND);
			mesHasSendInfo.put("server", MessageConfig.currentServer);
			mesHasSendInfo.put("clienttype",fromclienttype);
			mesHasSendInfo.put("message", "群消息已发送");
			session.sendMessage(new TextMessage(mesHasSendInfo.toString()));

			logger.info("发出消息回执："+mesHasSendInfo.toString());

		} else if (MessageType.TEXT_RESULT.equalsIgnoreCase(jo.getString("messagetype"))
				|| MessageType.GROUP_TXT_RESULT.equalsIgnoreCase(jo.getString("messagetype"))
				|| MessageType.VIEW_TEXT.equalsIgnoreCase(jo.getString("messagetype"))
				|| MessageType.GROUP_TXT_VIEW.equalsIgnoreCase(jo.getString("messagetype"))) {// 消息反馈,处理放入单独队列


			// 判断电脑端是否存在
			String pcFlag = null;
			String mobileFlag = null;
			try {
				pcFlag = MessageConfig.getServer((String)jo.get("toid"), "pc");
				mobileFlag = MessageConfig.getServer((String)jo.get("toid"), "mobile");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (StringUtils.hasLength(pcFlag)&& StringUtils.hasLength(mobileFlag)) {// 手机和电脑都在线
				jo.put("clienttype", "pc");
				MessageConfig.sendResponseToMQ(pcFlag+CommonConstants.QUEUE_SUFFIX_NAME_RESPTEXT, jo.toString());
				jo.put("clienttype", "mobile");
				MessageConfig.sendResponseToMQ(mobileFlag+CommonConstants.QUEUE_SUFFIX_NAME_RESPTEXT, jo.toString());

			} else if (!StringUtils.hasLength(pcFlag)&& StringUtils.hasLength(mobileFlag)) {// 手机在线，电脑不在线
				logger.info("接受者手机端在线===================");
				jo.put("clienttype", "mobile");
				logger.info("放入mq===================");
				MessageConfig.sendResponseToMQ(mobileFlag+CommonConstants.QUEUE_SUFFIX_NAME_RESPTEXT, jo.toString());
			} else if (StringUtils.hasLength(pcFlag)&& !StringUtils.hasLength(mobileFlag)) {// 手机不在线，电脑在线
				jo.put("clienttype", "pc");
				MessageConfig.sendResponseToMQ(pcFlag+CommonConstants.QUEUE_SUFFIX_NAME_RESPTEXT, jo.toString());
			}


		}else if(MessageType.MESSAGE_TYPE_REVOKE.equalsIgnoreCase(jo.getString("messagetype"))){
			//			撤回消息指令
			logger.info("进入消息撤回监听器。。。。。。。。。。。");

			// 判断电脑端是否存在
			String pcFlag = null;
			String mobileFlag = null;
			try {
				pcFlag = MessageConfig.getServer((String)jo.get("toid"), "pc");
				mobileFlag = MessageConfig.getServer((String)jo.get("toid"), "mobile");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (StringUtils.hasLength(pcFlag)&& StringUtils.hasLength(mobileFlag)) {// 手机和电脑都在线
				jo.put("clienttype", "pc");
				MessageConfig.sendRevokeMsgToMQ(pcFlag+CommonConstants.QUEUE_SUFFIX_NAME_REVOKETEXT, jo.toString());
				jo.put("clienttype", "mobile");
				MessageConfig.sendRevokeMsgToMQ(mobileFlag+CommonConstants.QUEUE_SUFFIX_NAME_REVOKETEXT, jo.toString());

			} else if (!StringUtils.hasLength(pcFlag)&& StringUtils.hasLength(mobileFlag)) {// 手机在线，电脑不在线
				logger.info("接受者手机端在线===================");
				jo.put("clienttype", "mobile");
				logger.info("放入mq===================");
				MessageConfig.sendRevokeMsgToMQ(mobileFlag+CommonConstants.QUEUE_SUFFIX_NAME_REVOKETEXT, jo.toString());
			} else if (StringUtils.hasLength(pcFlag)&& !StringUtils.hasLength(mobileFlag)) {// 手机不在线，电脑在线
				jo.put("clienttype", "pc");
				MessageConfig.sendRevokeMsgToMQ(pcFlag+CommonConstants.QUEUE_SUFFIX_NAME_REVOKETEXT, jo.toString());
			}


		} else {// 发送消息

			logger.info("进入单聊============================");

			String toid = jo.getString("toid");
			String messageid = jo.getString("messageid");

			if(toid==null||messageid==null){
				JSONObject textMsgResult = new JSONObject();
				textMsgResult.put("messagetype",MessageType.TEXT_RESULT);
				textMsgResult.put("status",CommonConstants.OPTION_RESULT_FAIL);
				textMsgResult.put("message", "参数toid和messageid不能为空");
				session.sendMessage(new TextMessage(textMsgResult.toString()));
				return;

			}
			logger.info("进入单聊============查询发送人头像开始================");
			try {
				//如果是聊天消息"businessType": "pointtopoint"或"businessType": "grouppointtopoint"，设置发送人消息头像
				JSONObject content = (JSONObject)jo.get("content");
				String businessType =  (String)content.get("businessType");

				if(businessType!=null&&(businessType.equals(CommonConstants.IM_BUSINESSTYPE_POINTTOPOINT)||businessType.equals(CommonConstants.IM_BUSINESSTYPE_GROUPPOINTTOPOINT))){
					Object headImgOwnerIdObj  = jo.get("fromid");
					Long headImgOwnerIdLong = null;
					if(headImgOwnerIdObj instanceof String){
						headImgOwnerIdLong = new Long((String)headImgOwnerIdObj);
					}else{
						headImgOwnerIdLong=(Long) headImgOwnerIdObj;
					}
					String redisCatchUserHeadImgAndImstatusJsonStr = RedisUtils.getString(CommonConstants.IM_MSG_FROMUSER_HEADIMGANDIMSTATUS + headImgOwnerIdLong);
					if(null!=redisCatchUserHeadImgAndImstatusJsonStr){
						jo.put("imheadimg",JSONObject.fromObject(redisCatchUserHeadImgAndImstatusJsonStr).get("imheadimg"));
						jo.put("imstatus",JSONObject.fromObject(redisCatchUserHeadImgAndImstatusJsonStr).get("imstatus"));
					}else{
						UserHeadImgDao userHeadImgDao = null;
						userHeadImgDao = (UserHeadImgDao) SpringManager.getService("userHeadImgDao");
						userHeadImgDao.setDBConnection(ConnectionManager.getNoThreadConnection("default"));
						Map<String,String> fromUserHeadImgAndImstatusInfo= userHeadImgDao.getHeadImgByUserId(headImgOwnerIdLong);
						//设置头像缓存
						RedisUtils.set(CommonConstants.IM_MSG_FROMUSER_HEADIMGANDIMSTATUS+headImgOwnerIdLong,JSONObject.fromObject(fromUserHeadImgAndImstatusInfo).toString(),CommonConstants.IM_USERHEADIMG_CATCH_TIMEOUT);
						jo.put("imheadimg",fromUserHeadImgAndImstatusInfo.get("imheadimg"));
						jo.put("imstatus",fromUserHeadImgAndImstatusInfo.get("imstatus"));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				jo.put("imheadimg",CommonConstants.getDefautHeadImg());
				jo.put("imstatus", CommonConstants.IM_STATUS_WORKING);
			}

			logger.info("进入单聊============查询发送人头像结束================"+jo.get("imheadimg"));

			// 判断电脑端是否存在
			String pcFlag = null;
			String mobileFlag = null;
			try {
				pcFlag = MessageConfig.getServer(toid, "pc");
				mobileFlag = MessageConfig.getServer(toid, "mobile");
			} catch (Exception e) {
				e.printStackTrace();
			}


			logger.info("在线标识pcFlag：=============================="+pcFlag);
			logger.info("在线标识mobileFlag：=============================="+mobileFlag);


			if (StringUtils.hasLength(pcFlag)
					&& StringUtils.hasLength(mobileFlag)) {// 手机和电脑都在线
				jo.put("clienttype", "pc");
				MessageConfig.sendToMQ(pcFlag+CommonConstants.QUEUE_SUFFIX_NAME_PTPMSG, jo.toString());
				jo.put("clienttype", "mobile");
				MessageConfig.sendToMQ(mobileFlag+CommonConstants.QUEUE_SUFFIX_NAME_PTPMSG, jo.toString());


			} else if (!StringUtils.hasLength(pcFlag)&& StringUtils.hasLength(mobileFlag)) {// 手机在线，电脑不在线
				logger.info("接受者手机端在线===================");
				jo.put("clienttype", "pc");
				RedisUtils.setMap(toid + "_pc", jo.getString("messageid"),jo.toString());//为了缓存离线消息

				jo.put("clienttype", "mobile");
				logger.info("放入mq===================");
				MessageConfig.sendToMQ(mobileFlag+CommonConstants.QUEUE_SUFFIX_NAME_PTPMSG, jo.toString());
				logger.info("放入mq成功===================");
			} else if (StringUtils.hasLength(pcFlag)
					&& !StringUtils.hasLength(mobileFlag)) {// 手机不在线，电脑在线
				jo.put("clienttype", "mobile");
				RedisUtils.setMap(toid + "_mobile", jo.getString("messageid"),jo.toString());
				jo.put("clienttype", "pc");
				MessageConfig.sendToMQ(pcFlag+CommonConstants.QUEUE_SUFFIX_NAME_PTPMSG, jo.toString());
			} else {// 手机和电脑不在线
				logger.info("手机和电脑都不在线=============");
                try {
                    jo.put("clienttype", "pc");
                    RedisUtils.setMap(toid + "_pc", jo.getString("messageid"),jo.toString());
                    jo.put("clienttype", "mobile");
                    RedisUtils.setMap(toid + "_mobile", jo.getString("messageid"),jo.toString());
                    //插入离线现象到数据库
					MessageConfig.sendOffLineMsgToMQ(jo.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

			}
			try {
                logger.info("===============开始发出消息回执==============");
				//给个消息已发送回执------add by lly
				JSONObject mesHasSendInfo = new JSONObject();
				mesHasSendInfo.put("messageid",jo.getString("messageid"));
				mesHasSendInfo.put("status", CommonConstants.OPTION_RESULT_SUCCESS);
				mesHasSendInfo.put("messagetype", MessageType.TEXT_HAS_SEND);
				mesHasSendInfo.put("server", MessageConfig.currentServer);
				mesHasSendInfo.put("message", "消息已发送");
				mesHasSendInfo.put("clienttype",fromclienttype);
				mesHasSendInfo.put("toid",jo.getString("fromid"));
				mesHasSendInfo.put("toname",jo.getString("fromname"));
				session.sendMessage(new TextMessage(mesHasSendInfo.toString()));
				System.out.println("发出消息回执结束："+mesHasSendInfo.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 系统登录 需要单独实现
	 * 
	 * @param jo
	 * @return
	 * @throws Exception
	 * todo
	 */
	private JSONObject login(JSONObject jo, WebSocketSession session)
			throws Exception {
		JSONObject result = new JSONObject();
        try {

            String userid = (String)jo.get("userid");
            String imToken = (String)jo.get("imToken");
            if(userid==null||imToken==null){
                result.put("status", CommonConstants.OPTION_RESULT_FAIL);
                result.put("messagetype", MessageType.LOGIN_RESULT);
                result.put("server", MessageConfig.currentServer);
                result.put("message", "参数userid,imToken不能为空");
            }else{
                //验证imToken合法性
                String reghtImToken = RedisUtils.getString(CommonConstants.IM_TOKEN_KEY_PREFIX + userid);
				logger.info("reghtImToken:"+reghtImToken+"=========imToken:"+imToken);
                if(null!=reghtImToken){
                    reghtImToken = reghtImToken.trim();

                    if(reghtImToken.equals(imToken)){
                        result.put("status", CommonConstants.OPTION_RESULT_SUCCESS);
                        result.put("messagetype", MessageType.LOGIN_RESULT);
                        result.put("server", MessageConfig.currentServer);
                        result.put("message", "通信身份验证成功");
                    }else{
                        result.put("status", CommonConstants.OPTION_RESULT_FAIL);
                        result.put("messagetype", MessageType.LOGIN_RESULT);
                        result.put("server", MessageConfig.currentServer);
                        result.put("message", "通信身份验证失败");
                    }

                }else{
                    result.put("status", CommonConstants.OPTION_RESULT_FAIL);
                    result.put("messagetype", MessageType.LOGIN_RESULT);
                    result.put("server", MessageConfig.currentServer);
                    result.put("message", "imToken不存在");
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
			result.put("status", CommonConstants.OPTION_RESULT_FAIL);
			result.put("messagetype", MessageType.LOGIN_RESULT);
			result.put("server", MessageConfig.currentServer);
			result.put("message", "登录异常");
			return  result;
        }

    }

	/**
	 * 系统登录 需要单独实现
	 *
	 * @param jo
	 * @return
	 * @throws Exception
	 * todo
	 */
	private void logout(JSONObject jo, WebSocketSession session)
			throws Exception {
		JSONObject logoutmesg = new JSONObject();
		logoutmesg.put("messagetype", MessageType.LOGIN_OUT_RESULT);
		logoutmesg.put("server", MessageConfig.currentServer);

		String userid = (String)jo.get("userid");
		String clienttype = (String)jo.get("clienttype");
		String imToken = (String)jo.get("imToken");
		if(userid==null||clienttype==null||imToken==null){
			logoutmesg.put("status", CommonConstants.OPTION_RESULT_FAIL);
			logoutmesg.put("message", "参数userid,clienttype,imToken不能为空");
			session.sendMessage(new TextMessage(logoutmesg.toString()));
		}
		//验证imToken合法性
		String reghtImToken = RedisUtils.getString(CommonConstants.IM_TOKEN_KEY_PREFIX + userid);
		if(null!=reghtImToken){
			reghtImToken = reghtImToken.trim();

			if(reghtImToken.equals(imToken)){
				//身份验证成功，确认是本账户退出登陆
				MessageConfig.deleteSession(userid,clienttype);
				logoutmesg.put("status",CommonConstants.OPTION_RESULT_SUCCESS);
				logoutmesg.put("message", "退出通信成功");
				session.sendMessage(new TextMessage(logoutmesg.toString()));
				MessageConfig.deleteSession(userid,clienttype);
			}else{
				//身份验证失败
				MessageConfig.deleteSession(userid,clienttype);
				logoutmesg.put("status", CommonConstants.OPTION_RESULT_FAIL);
				logoutmesg.put("message", "身份验证失败");
				session.sendMessage(new TextMessage(logoutmesg.toString()));
			}

		}


	}


	/**
	 * 接收消息异常
	 */
	@Override
	public void handleTransportError(WebSocketSession wss, Throwable arg1)
			throws Exception {
		try {
			if (MessageConfig.machineList.containsValue(wss)) {
				Iterator<Entry<String, WebSocketSession>> it = MessageConfig.machineList
						.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, WebSocketSession> data = it.next();
					if (data.getValue().equals(wss)) {
						MessageConfig.machineList.remove(data.getKey());
						MessageConfig.deleteSession(data.getKey(), "pc");
					}
				}
			}
			if (MessageConfig.mobileList.containsValue(wss)) {
				Iterator<Entry<String, WebSocketSession>> it = MessageConfig.mobileList
						.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, WebSocketSession> data = it.next();
					if (data.getValue().equals(wss)) {
						MessageConfig.mobileList.remove(data.getKey());
						MessageConfig.deleteSession(data.getKey(), "mobile");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		// arg1.printStackTrace();
		logger.debug("消息传输错误，连接关闭--" + DateTimeUtils.getCurrentLongDate());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}







}
