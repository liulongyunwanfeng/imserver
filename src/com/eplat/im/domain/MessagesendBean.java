package com.eplat.im.domain;

import com.eplat.annotation.FieldInfo;
import com.eplat.annotation.TableInfo;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @类名称：MessagesendBean
 * @类描述：消息记录
 * @创建人：$author
 * @创建时间：2017-09-22 22:24:58
 */
@TableInfo(tablename = "IM_MESSAGE_SEND", tabledesc = "消息记录", logtype = "default")
public class MessagesendBean extends ImPacket implements Comparable<MessagesendBean> {
	/**
	 * @字段名称：SEQ_ID
	 * @属性名称：seqid
	 * @字段类型：BigDecimal
	 * @字段长度：19
	 * @字段描述：消息记录流水号
	 */
	@FieldInfo(fieldid = "SEQ_ID", name = "消息记录流水号", logflag = true, propid = "seqid", fieldtype = "BigDecimal")
	private BigDecimal seqid;
	/**
	 * @字段名称：MESSAGE_ID
	 * @属性名称：messageid
	 * @字段类型：String
	 * @字段长度：36
	 * @字段描述：消息ID
	 */
	@FieldInfo(fieldid = "MESSAGE_ID", name = "消息ID", logflag = true, propid = "messageid", fieldtype = "String")
	private String messageid;
	/**
	 * @字段名称：MESSAGE_TYPE
	 * @属性名称：messagetype
	 * @字段类型：String
	 * @字段长度：3
	 * @字段描述：消息类型0文字1文件2图片
	 */
	@FieldInfo(fieldid = "MESSAGE_TYPE", name = "消息类型0文字1文件2图片", logflag = true, propid = "messagetype", fieldtype = "String")
	private String messagetype;
	/**
	 * @字段名称：FROM_ID
	 * @属性名称：fromid
	 * @字段类型：String
	 * @字段长度：36
	 * @字段描述：发送者ID
	 */
	@FieldInfo(fieldid = "FROM_ID", name = "发送者ID", logflag = true, propid = "fromid", fieldtype = "String")
	private String fromid;
	/**
	 * @字段名称：FROM_NAME
	 * @属性名称：fromname
	 * @字段类型：String
	 * @字段长度：30
	 * @字段描述：发送者名字
	 */
	@FieldInfo(fieldid = "FROM_NAME", name = "发送者名字", logflag = true, propid = "fromname", fieldtype = "String")
	private String fromname;
	/**
	 * @字段名称：TO_ID
	 * @属性名称：toid
	 * @字段类型：String
	 * @字段长度：36
	 * @字段描述：接受者ID
	 */
	@FieldInfo(fieldid = "TO_ID", name = "接受者ID", logflag = true, propid = "toid", fieldtype = "String")
	private String toid;
	/**
	 * @字段名称：TO_NAME
	 * @属性名称：toname
	 * @字段类型：String
	 * @字段长度：30
	 * @字段描述：接受者名字
	 */
	@FieldInfo(fieldid = "TO_NAME", name = "接受者名字", logflag = true, propid = "toname", fieldtype = "String")
	private String toname;
	/**
	 * @字段名称：CONTENT
	 * @属性名称：content
	 * @字段类型：String
	 * @字段长度：2000
	 * @字段描述：消息内容
	 */
	@FieldInfo(fieldid = "CONTENT", name = "消息内容", logflag = true, propid = "content", fieldtype = "String")
	private String content;
	/**
	 * @字段名称：SEND_DATE
	 * @属性名称：senddate
	 * @字段类型：Timestamp
	 * @字段长度：23
	 * @字段描述：发送时间
	 */
	@FieldInfo(fieldid = "SEND_DATE", name = "发送时间", logflag = true, propid = "senddate", fieldtype = "Timestamp")
	private Timestamp senddate;
	/**
	 * @字段名称：ACCPET_DATE
	 * @属性名称：accpetdate
	 * @字段类型：Timestamp
	 * @字段长度：23
	 * @字段描述：接收时间
	 */
	@FieldInfo(fieldid = "ACCPET_DATE", name = "接收时间", logflag = true, propid = "accpetdate", fieldtype = "Timestamp")
	private Timestamp accpetdate;
	/**
	 * @字段名称：ACCEPT_STATE
	 * @属性名称：acceptstate
	 * @字段类型：String
	 * @字段长度：3
	 * @字段描述：接收状态0未接收1已接收
	 */
	@FieldInfo(fieldid = "ACCEPT_STATE", name = "接收状态0未接收1已接收", logflag = true, propid = "acceptstate", fieldtype = "String")
	private String acceptstate;
	@FieldInfo(fieldid = "VIEW_DATE", name = "查看时间", logflag = true, propid = "viewdate", fieldtype = "Timestamp")
	private Timestamp viewdate;
	/**
	 * @字段名称：ACCEPT_STATE
	 * @属性名称：acceptstate
	 * @字段类型：String
	 * @字段长度：3
	 * @字段描述：接收状态0未接收1已接收
	 */
	@FieldInfo(fieldid = "VIEW_STATE", name = "查看状态", logflag = true, propid = "viewstate", fieldtype = "String")
	private String viewstate;
	/**
	 * @字段名称：CLIENT_TYPE
	 * @属性名称：clienttype
	 * @字段类型：String
	 * @字段长度：3
	 * @字段描述：pc 电脑端 mobile 手机端移动端
	 */
	@FieldInfo(fieldid = "CLIENT_TYPE", name = "客户端类型", logflag = true, propid = "clienttype", fieldtype = "String")
	private String clienttype;

	/**
	 * @字段名称：BUSINESS_TYPE
	 * @属性名称：businesstype
	 * @字段类型：String
	 * @字段长度：20
	 * @字段描述：业务消息类型
	 *
	 * liulongyun ------扩展
	 */
	@FieldInfo(fieldid = "BUSINESS_TYPE", name = "消息的业务类型", logflag = true, propid = "businesstype", fieldtype = "String")
	private String businesstype;


	
	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

	public Timestamp getViewdate() {
		return viewdate;
	}

	public void setViewdate(Timestamp viewdate) {
		this.viewdate = viewdate;
	}

	public String getViewstate() {
		return viewstate;
	}

	public void setViewstate(String viewstate) {
		this.viewstate = viewstate;
	}

	public BigDecimal getSeqid() {
		return this.seqid;
	}

	public void setSeqid(BigDecimal seqid) {
		this.seqid = seqid;
	}

	public String getMessageid() {
		return this.messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public String getMessagetype() {
		return this.messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getFromid() {
		return this.fromid;
	}

	public void setFromid(String fromid) {
		this.fromid = fromid;
	}

	public String getFromname() {
		return this.fromname;
	}

	public void setFromname(String fromname) {
		this.fromname = fromname;
	}

	public String getToid() {
		return this.toid;
	}

	public void setToid(String toid) {
		this.toid = toid;
	}

	public String getToname() {
		return this.toname;
	}

	public void setToname(String toname) {
		this.toname = toname;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getSenddate() {
		return this.senddate;
	}

	public void setSenddate(Timestamp senddate) {
		this.senddate = senddate;
	}

	public Timestamp getAccpetdate() {
		return this.accpetdate;
	}

	public void setAccpetdate(Timestamp accpetdate) {
		this.accpetdate = accpetdate;
	}

	public String getAcceptstate() {
		return this.acceptstate;
	}

	public void setAcceptstate(String acceptstate) {
		this.acceptstate = acceptstate;
	}


	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	@Override
	public int compareTo(MessagesendBean o) {
		if (this.senddate.getTime()-o.getSenddate().getTime()>0){
			return 1;
		} else if (this.senddate.getTime()-o.getSenddate().getTime()==0){
			return 0;
		} else {
			return -1;
		}
		
	}
}
