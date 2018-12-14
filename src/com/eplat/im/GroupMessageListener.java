package com.eplat.im;

import com.eplat.db.ConnectionManager;
import com.eplat.db.DBConnection;
import com.eplat.im.dao.GroupacceptDao;
import com.eplat.im.dao.GroupsendDao;
import com.eplat.im.domain.GroupacceptBean;
import com.eplat.im.domain.GroupsendBean;
import com.eplat.utils.DateTimeUtils;
import com.eplat.utils.RedisUtils;
import com.eplat.utils.SpringManager;
import com.eplat.utils.StringUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

/**
 * 群组消息监听处理类
 * 
 * @author Administrator
 *
 */
public class GroupMessageListener {
	private Logger logger = Logger.getLogger(MessageListener.class);

	/**
	 * 写入用户群组的发送记录
	 * 
	 * @param connection
	 * @param groupsend
	 * @throws Exception
	 */
	private void writeGroupSend(DBConnection connection, GroupsendBean groupsend)
			throws Exception {
		GroupsendDao groupsendDao = (GroupsendDao) SpringManager
				.getService("groupsendDao");
		groupsendDao.setDBConnection(connection);
		
		groupsendDao.insert(groupsend);
		RedisUtils.set(groupsend.getGroupmsgid(), JSONObject.fromObject(groupsend).toString());
	}

	private void writeGroupAccept(DBConnection connection,
			List<GroupacceptBean> list) throws Exception {
		GroupacceptDao groupacceptDao = (GroupacceptDao) SpringManager
				.getService("groupacceptDao");
		groupacceptDao.setDBConnection(connection);
		groupacceptDao.insertBatch(list);
		
	}

	public void receiveMessage(String msg) {
		DBConnection connection = null;
		try {
			JSONObject jo = JSONObject.fromObject(msg);
			if (MessageType.GROUP_TXT.equalsIgnoreCase(jo
					.getString("messagetype"))) {

				GroupsendBean groupsend = new GroupsendBean();
				groupsend.setFromid(jo.getString("fromid"));
				groupsend.setGroupseqid(new BigDecimal(UUID.randomUUID()
						.getMostSignificantBits()));
				groupsend.setGroupmsgid(jo.getString("messageid"));
				groupsend.setContent(jo.getString("content"));
				groupsend.setContentJsonObj(jo.getJSONObject("content"));
				groupsend.setFromname(jo.getString("fromname"));
				groupsend.setMessagetype(MessageType.GROUP_TXT);
				groupsend.setSenddate(DateTimeUtils.strToSqlLongDate(jo.getString("senddate")));
				groupsend.setStatus(0);
				groupsend.setTogroupid(jo.getString("togroupid"));
				groupsend.setTogroupname(jo.getString("togroupname"));


				groupsend.setAcceptnum(1);// 认为发送人已接收，所以接收数量一开始就是1
				groupsend.setViewnum(1);// 认为发送人已查看，所以查看数量一开始就是1


				connection = ConnectionManager.getNoThreadConnection("default");
				IGroupMemeber groupMember = (IGroupMemeber) SpringManager.getService("groupMemeber");
				groupMember.setConnection(connection);

				Map<String, String> list = groupMember.getList(groupsend
						.getTogroupid());

				groupsend.setGroupnum(list.size());

				this.writeGroupSend(connection, groupsend);//写入发送表

				if (list != null && list.size() > 0) {// 写入接收表
					List<GroupacceptBean> acceptList=new ArrayList<GroupacceptBean>();
					Iterator<Entry<String, String>> it = list.entrySet()
							.iterator();
					while (it.hasNext()) {
						Entry<String, String> memberdata = it.next();

						String userid = memberdata.getKey();
						String username = memberdata.getValue();

						GroupacceptBean groupacceptBean = new GroupacceptBean();
						groupacceptBean.setAcceptseqid(new BigDecimal(UUID.randomUUID().getMostSignificantBits()));


						groupacceptBean.setContent(groupsend.getContent());
						groupacceptBean.setContentJsonObj(groupsend.getContentJsonObj());
						groupacceptBean.setFromid(groupsend.getFromid());
						groupacceptBean.setFromname(groupsend.getFromname());
						groupacceptBean.setGroupmsgid(groupsend.getGroupmsgid());

						groupacceptBean.setMessagetype(MessageType.GROUP_TXT_USER);

						groupacceptBean.setSenddate(groupsend.getSenddate());
						groupacceptBean.setStatus(groupsend.getStatus());
						groupacceptBean.setTogroupid(groupsend.getTogroupid());
						groupacceptBean.setTogroupname(groupsend.getTogroupname());

						groupacceptBean.setToid(userid);
						groupacceptBean.setToname(username);



						// 群消息发送，对自己的群消息，设置为以接收和已查看
						if(groupacceptBean.getFromid().equals(groupacceptBean.getToid())){
							groupacceptBean.setViewstate("1");
                            groupacceptBean.setAcceptstate("1");
                            groupacceptBean.setAccpetdate(new Timestamp(System.currentTimeMillis()));
                            groupacceptBean.setViewdate(new Timestamp(System.currentTimeMillis()));
						}else{
							groupacceptBean.setViewstate("0");
                            groupacceptBean.setAcceptstate("0");
							groupacceptBean.setViewdate(null);
                            groupacceptBean.setAccpetdate(null);
						}

                        JSONObject jo1 = JSONObject.fromObject(groupacceptBean);
						jo1.put("imheadimg",jo.get("imheadimg"));
						String pcFlag = MessageConfig.getServer(userid, "pc");
						String mobileFlag = MessageConfig.getServer(userid,"mobile");
						acceptList.add(groupacceptBean);
						//写入到Redis,用于更新状态
						RedisUtils.set(groupsend.getGroupmsgid()+"_"+userid,JSONObject.fromObject(groupacceptBean).toString() );
						//System.out.println(jo1.toString());
						jo1.put("content",jo1.get("contentJsonObj"));
						jo1.remove("contentJsonObj");
						//System.out.println(jo1.toString());

						if (StringUtils.hasLength(pcFlag)&&StringUtils.hasLength(mobileFlag)) {// 手机和电脑在线
							jo1.put("clienttype", "pc");
							MessageConfig.sendToMQ(pcFlag, jo1.toString());
							jo1.put("clienttype", "mobile");
							MessageConfig.sendToMQ(mobileFlag, jo1.toString());
						} else if (StringUtils.hasLength(mobileFlag)&&!StringUtils.hasLength(pcFlag)) {// 手机在线,电脑不在线
							jo1.put("clienttype", "mobile");
							MessageConfig.sendToMQ(mobileFlag, jo1.toString());
							jo1.put("clienttype", "pc");
							RedisUtils.setMap(userid + "_pc",groupacceptBean.getGroupmsgid(), jo1.toString());
						} else if (!StringUtils.hasLength(mobileFlag)&&StringUtils.hasLength(pcFlag)) {// 手机不在线,电脑在线
							jo1.put("clienttype", "pc");
							MessageConfig.sendToMQ(pcFlag, jo1.toString());
							jo1.put("clienttype", "mobile");
							RedisUtils.setMap(userid + "_mobile",groupacceptBean.getGroupmsgid(), jo1.toString());
						}  else {// 没有在线,直接放入Redis缓存中,手机端和移动端都放

							logger.info("群成员移动端和PC端都不在线==============");
							jo1.put("clienttype", "pc");
							RedisUtils.setMap(userid + "_pc",groupacceptBean.getGroupmsgid(), jo1.toString());
							jo1.put("clienttype", "mobile");
							RedisUtils.setMap(userid + "_mobile",groupacceptBean.getGroupmsgid(), jo1.toString());
						}
					}
					this.writeGroupAccept(connection, acceptList);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("订阅群组消息处理：" + e.getMessage());
		} finally {
			try {
				if (connection != null) {
					connection.closeConnection();
				}
			} catch (Exception e) {
				logger.error("订阅群组消息处理：" + e.getMessage());
			}
		}
	}
}
