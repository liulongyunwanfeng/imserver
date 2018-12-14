package com.eplat;

import com.eplat.db.ConnectionManager;
import com.eplat.db.DBConnection;
import com.eplat.im.MessageConfig;
import com.eplat.im.MessageType;
import com.eplat.im.dao.MessageacceptDao;
import com.eplat.im.dao.MessagesendDao;
import com.eplat.im.domain.MessagesendBean;
import com.eplat.utils.DateTimeUtils;
import com.eplat.utils.SpringManager;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * 将离线消息放入mq在mq的监听器里将数据插入数据库
 */
public class OffLineMessageListener {
    private Logger logger = Logger.getLogger(OffLineMessageListener.class);

    public void receiveMessage(String msg) {

       logger.debug("消息监听收到消息：==========="+msg);
        try {
            JSONObject jo = JSONObject.fromObject(msg);
            //todo
            System.out.println("接收到离线消息:"+msg);
            insertMsgtoDbW(jo);
            logger.debug("离线消息插入数据库成功==============");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("离线消息插入异常：" + e.getMessage());
        }
    }


    public void insertMsgtoDbW(JSONObject jo) throws Exception{
        DBConnection connection =null;
        try {
            //当PC端后手机都不在线是直接插入数据库
            MessagesendBean send = new MessagesendBean();
            send.setSeqid(new BigDecimal(UUID.randomUUID().getMostSignificantBits()));
            send.setMessageid(jo.getString("messageid"));
            send.setMessagetype(jo.getString("messagetype"));
            send.setFromid(jo.getString("fromid"));
            send.setFromname(jo.getString("fromname"));
            send.setToid(jo.getString("toid"));
            send.setToname(jo.getString("toname"));
            send.setContent(jo.getString("content"));

            java.sql.Timestamp senddate = DateTimeUtils.strToSqlLongDate(jo.getString("senddate"));
            if(null==senddate){
                senddate =  new Timestamp(System.currentTimeMillis());
            }

            send.setSenddate(senddate);
            send.setAcceptstate("0");
            send.setViewstate("0");

            JSONObject contentJsonObj= null;
            try {
                contentJsonObj = JSONObject.fromObject(jo.getString("content"));
            } catch (Exception e) {
                e.printStackTrace();
                JSONObject jo1 = new JSONObject();
                jo1.put("status", "-1");
                jo1.put("messagetype", MessageType.ERROR_MSG);
                jo1.put("server", MessageConfig.currentServer);
                jo1.put("message", "消息体格式失败");
                throw  new Exception(jo1.toString());

            }
            try {
                logger.debug("插入数据库=============");
                send.setBusinesstype(contentJsonObj.getString("businessType"));//liulongyun扩展
                connection = ConnectionManager.getNoThreadConnection("default");
                connection.beginTrans();
                MessageacceptDao messageacceptDao = (MessageacceptDao) SpringManager.getService("messageacceptDao");
                messageacceptDao.setDBConnection(connection);
                messageacceptDao.insert(send);
                MessagesendDao messagesendDao = (MessagesendDao) SpringManager.getService("messagesendDao");
                messagesendDao.setDBConnection(connection);
                messagesendDao.insert(send);
                connection.commit();
                logger.debug("插入完成=============");
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
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


}
