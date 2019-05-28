package com.eplat.im;

import com.eplat.db.ConnectionManager;
import com.eplat.db.DBConnection;
import com.eplat.im.dao.GroupacceptDao;
import com.eplat.im.dao.GroupsendDao;
import com.eplat.im.dao.MessageacceptDao;
import com.eplat.im.dao.MessagesendDao;
import com.eplat.im.domain.GroupacceptBean;
import com.eplat.im.domain.GroupsendBean;
import com.eplat.im.domain.MessagesendBean;
import com.eplat.utils.RedisUtils;
import com.eplat.utils.SpringManager;
import com.eplat.utils.StringUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 消息接收处理-----消费者
 * 消息撤回mq监听器
 *
 *
 * 
 * @author Administrator
 *
 */
public class RevokeListener {
	private Logger logger = Logger.getLogger(RevokeListener.class);

	public void receiveMessage(String msg) {

		logger.info("收到撤回消息指令："+msg);

		/**
		 * 撤回消息业务逻辑：
		 * 判断撤回人是否是消息发送人。是：执行撤回，否：不执行撤回。
		 * 执行撤回
		 *
		 * 查询出需要撤回的消息，判断是否在撤回时间内（发送后三分钟）。
		 * 将其viewType改为serviceSystem，消息内容改为XXX撤回了一条消息。
		 * 并把撤回消息指令发送到对方，让对方执行相应的撤回操作（在会话里面将原消息显示为XXX撤回了一条消息，并修改显示样式为系统消息样式）
		 * */
		try {
			JSONObject revokeMsgJsonObj = JSONObject.fromObject(msg);

			boolean isRevokeMsgLegal =  isRevokeMsgLegal(revokeMsgJsonObj);
			/**
			 * 如果撤回的消息是群聊消息是公用一个撤回消息队列，业务逻辑判断之后会加一个标识hasFindServer,
			 * 再重新放回撤回队列里处理。下一次就会想处理单聊撤回一样处理
			 */
			Object hasFindServer = revokeMsgJsonObj.get("hasFindServer");

			if(isRevokeMsgLegal){
				String revokemsgtype = (String)revokeMsgJsonObj.get("revokemsgtype");
				if(CommonConstants.IM_BUSINESSTYPE_GROUPPOINTTOPOINT.equals(revokemsgtype)&&hasFindServer==null){
					//执行群消息撤回逻辑
					revokeGroupMsg(revokeMsgJsonObj);
				}else{
					//执行单聊消息撤回逻辑
					revokePtpMsg(revokeMsgJsonObj);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 撤回单聊消息
	 * 根据消息id查询消息，
	 * 1：判断消息的发送人是否和fromid一致。
	 * 2：判断消息是否在可撤回时间内（发送后三分钟）
	 * 3：撤回消息：将消息的viewType改为serviceSystem,将内容改为XXX撤回了一条消息
	 * @param revokeMsgJsonObj
	 */
	private void revokePtpMsg(JSONObject revokeMsgJsonObj) throws Exception{
		DBConnection connection =null;
		logger.info("执行单聊撤回操作。。。。。。。。。。。。。。。。。。。。");
		try {


			String messageid = (String)revokeMsgJsonObj.get("messageid");
			String fromid = (String)revokeMsgJsonObj.get("fromid");
			String toid = (String)revokeMsgJsonObj.get("toid");
			String[] queryParams = {messageid,toid};
			//当PC端后手机都不在线是直接插入数据库
			connection = ConnectionManager.getNoThreadConnection("default");
			connection.beginTrans();
			MessageacceptDao messageacceptDao = (MessageacceptDao) SpringManager.getService("messageacceptDao");
			messageacceptDao.setDBConnection(connection);
			List<MessagesendBean> acceptMsgs =  messageacceptDao.queryByCause("AND MESSAGE_ID = ? AND TO_ID=?",queryParams);

			for (int i = 0; i < acceptMsgs.size(); i++) {
				if(fromid.equals(acceptMsgs.get(i).getFromid())){//判断是否是发送人本身
					if(System.currentTimeMillis()-acceptMsgs.get(i).getSenddate().getTime()<CommonConstants.REVOKE_TIME){
						JSONObject content = JSONObject.fromObject(acceptMsgs.get(i).getContent());
						JSONObject viewContent = (JSONObject)content.get("viewContent");
						viewContent.put("content",acceptMsgs.get(i).getFromname()+"撤回了一条信息");
						viewContent.put("viewType",CommonConstants.MSG_VIEW_TYPE_SERVICESYSTEM);
						content.put("viewContent",viewContent);
						acceptMsgs.get(i).setContent(content.toString());
						//更新消息接收表
						messageacceptDao.update(acceptMsgs.get(i));
					}

				}
			}

			MessagesendDao messagesendDao = (MessagesendDao) SpringManager.getService("messagesendDao");
			messagesendDao.setDBConnection(connection);
			List<MessagesendBean> sendMsgs =  messagesendDao.queryByCause("AND MESSAGE_ID = ? AND TO_ID=?",queryParams);
			for (int i = 0; i < sendMsgs.size(); i++) {
				if(fromid.equals(sendMsgs.get(i).getFromid())){//判断是否是发送人本身
					if(System.currentTimeMillis()-sendMsgs.get(i).getSenddate().getTime()<CommonConstants.REVOKE_TIME){
						JSONObject content = JSONObject.fromObject(sendMsgs.get(i).getContent());
						JSONObject viewContent = (JSONObject)content.get("viewContent");
						viewContent.put("content",acceptMsgs.get(i).getFromname()+"撤回了一条信息");
						viewContent.put("viewType",CommonConstants.MSG_VIEW_TYPE_SERVICESYSTEM);
						content.put("viewContent",viewContent);
						sendMsgs.get(i).setContent(content.toString());
						//更新消息发送表
						messagesendDao.update(sendMsgs.get(i));
					}
				}
			}
			connection.commit();

			WebSocketSession pcsession=MessageConfig.getSession(toid,CommonConstants.IM_CLIENT_TYPE_PC);
			WebSocketSession mobilesession=MessageConfig.getSession(toid,CommonConstants.IM_CLIENT_TYPE_MOBILE);
			if(pcsession!=null){
				pcsession.sendMessage(new TextMessage(revokeMsgJsonObj.toString()));

			}
			if(mobilesession!=null){
				mobilesession.sendMessage(new TextMessage(revokeMsgJsonObj.toString()));

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(null!=connection){
				connection.closeConnection();
			}
		}

	}

	/**
	 * 撤回群聊消息
	 * @param revokeMsgJsonObj
	 * @throws Exception
	 */
	private void revokeGroupMsg(JSONObject revokeMsgJsonObj) throws Exception{

		logger.info("执行群聊撤回操作。。。。。。。。。。。。。。。。。。。。");
		DBConnection connection =null;
		try {
			String messageid = (String)revokeMsgJsonObj.get("messageid");
			String fromid = (String)revokeMsgJsonObj.get("fromid");
			String toid = (String)revokeMsgJsonObj.get("toid");
			String[] queryParams = {messageid,toid};


			connection = ConnectionManager.getNoThreadConnection("default");
			connection.beginTrans();
			//更新发送表
			GroupsendDao groupsenddao = (GroupsendDao) SpringManager.getService("GroupsendDao");
			groupsenddao.setDBConnection(connection);
			List<GroupsendBean> groupsendmsgs =  groupsenddao.queryByCause("AND GROUP_MSG_ID = ? AND TO_GROUP_ID=?",queryParams);

			for (int i = 0; i < groupsendmsgs.size(); i++) {
				if(fromid.equals(groupsendmsgs.get(i).getFromid())){//判断是否是发送人本身
					if(System.currentTimeMillis()-groupsendmsgs.get(i).getSenddate().getTime()<CommonConstants.REVOKE_TIME){
						JSONObject content = JSONObject.fromObject(groupsendmsgs.get(i).getContent());
						JSONObject viewContent = (JSONObject)content.get("viewContent");
						viewContent.put("content",groupsendmsgs.get(i).getFromname()+"撤回了一条信息");
						viewContent.put("viewType",CommonConstants.MSG_VIEW_TYPE_SERVICESYSTEM);
						content.put("viewContent",viewContent);
						groupsendmsgs.get(i).setContent(content.toString());
						//更新消息发送表
						groupsenddao.update(groupsendmsgs.get(i));

					}

				}
			}

			//接收表
			GroupacceptDao groupacceptdao = (GroupacceptDao) SpringManager.getService("GroupacceptDao");
			groupacceptdao.setDBConnection(connection);
			List<GroupacceptBean> groupacceptmsgs =  groupacceptdao.queryByCause("AND GROUP_MSG_ID = ? AND TO_GROUP_ID=?",queryParams);

			for (int i = 0; i < groupacceptmsgs.size(); i++) {
				if(fromid.equals(groupacceptmsgs.get(i).getFromid())){//判断是否是发送人本身
					if(System.currentTimeMillis()-groupacceptmsgs.get(i).getSenddate().getTime()<CommonConstants.REVOKE_TIME){
						JSONObject content = JSONObject.fromObject(groupacceptmsgs.get(i).getContent());
						JSONObject viewContent = (JSONObject)content.get("viewContent");
						viewContent.put("content",groupacceptmsgs.get(i).getFromname()+"撤回了一条信息");
						viewContent.put("viewType",CommonConstants.MSG_VIEW_TYPE_SERVICESYSTEM);
						content.put("viewContent",viewContent);
						groupacceptmsgs.get(i).setContent(content.toString());




						//revokeMsgJsonObj加个标识再一次丢到队列里去
						revokeMsgJsonObj.put("hasFindServer",true);

						String pcFlag = null;
						String mobileFlag = null;
						try {
							pcFlag = MessageConfig.getServer(groupacceptmsgs.get(i).getToid(), "pc");
							mobileFlag = MessageConfig.getServer(groupacceptmsgs.get(i).getToid(), "mobile");
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (StringUtils.hasLength(pcFlag)&& StringUtils.hasLength(mobileFlag)) {// 手机和电脑都在线
							revokeMsgJsonObj.put("clienttype", "pc");
							MessageConfig.sendRevokeMsgToMQ(pcFlag+CommonConstants.QUEUE_SUFFIX_NAME_REVOKETEXT, revokeMsgJsonObj.toString());
							revokeMsgJsonObj.put("clienttype", "mobile");
							MessageConfig.sendRevokeMsgToMQ(mobileFlag+CommonConstants.QUEUE_SUFFIX_NAME_REVOKETEXT, revokeMsgJsonObj.toString());

						} else if (!StringUtils.hasLength(pcFlag)&& StringUtils.hasLength(mobileFlag)) {// 手机在线，电脑不在线
							logger.info("接受者手机端在线===================");
							revokeMsgJsonObj.put("clienttype", "mobile");
							logger.info("放入mq===================");
							MessageConfig.sendRevokeMsgToMQ(mobileFlag+CommonConstants.QUEUE_SUFFIX_NAME_REVOKETEXT, revokeMsgJsonObj.toString());
						} else if (StringUtils.hasLength(pcFlag)&& !StringUtils.hasLength(mobileFlag)) {// 手机不在线，电脑在线
							revokeMsgJsonObj.put("clienttype", "pc");
							MessageConfig.sendRevokeMsgToMQ(pcFlag+CommonConstants.QUEUE_SUFFIX_NAME_REVOKETEXT, revokeMsgJsonObj.toString());
						}

					}

				}
			}


			if(groupacceptmsgs.size()>0){
				//执行批量更新消息接收表
				groupacceptdao.updateBatch(groupacceptmsgs);

			}

			connection.commit();

			WebSocketSession pcsession=MessageConfig.getSession(toid,CommonConstants.IM_CLIENT_TYPE_PC);
			WebSocketSession mobilesession=MessageConfig.getSession(toid,CommonConstants.IM_CLIENT_TYPE_MOBILE);




			if(pcsession!=null){
				pcsession.sendMessage(new TextMessage(revokeMsgJsonObj.toString()));

			}
			if(mobilesession!=null){
				mobilesession.sendMessage(new TextMessage(revokeMsgJsonObj.toString()));

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(null!=connection){
				connection.closeConnection();
			}
		}


	}


	/**
	 * 判断撤回消息指令是否合法
	 * @param revokeMsgJsonObj
	 * @return
	 * @throws Exception
	 */

	private  boolean isRevokeMsgLegal(JSONObject revokeMsgJsonObj) throws Exception{
		if(null==revokeMsgJsonObj.get("messageid")){
			return  false;
		}
		if(null==revokeMsgJsonObj.get("messagetype")){
			return  false;
		}
		if(null==revokeMsgJsonObj.get("revokemsgtype")){
			return  false;
		}
		if(null==revokeMsgJsonObj.get("fromid")){
			return  false;
		}if(null==revokeMsgJsonObj.get("fromname")){
			return  false;
		}
		if(null==revokeMsgJsonObj.get("toid")){
			return  false;
		}
		if(null==revokeMsgJsonObj.get("toname")){
			return  false;
		}
		return  true;
	}


}
