package com.eplat.im.domain;

import com.eplat.annotation.FieldInfo;
import com.eplat.annotation.TableInfo;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @类名称：GroupacceptBean
 * @类描述：群组消息接收
 * @创建人：$author
 * @创建时间：2017-09-22 22:24:50
 */
@TableInfo(tablename = "IM_GROUP_ACCEPT", tabledesc = "群组消息接收", logtype = "default")
public class GroupacceptBean extends ImPacket {
	/**
	 * @字段名称：ACCEPT_SEQ_ID
	 * @属性名称：acceptseqid
	 * @字段类型：BigDecimal
	 * @字段长度：19
	 * @字段描述：接收记录ID
	 */
	@FieldInfo(fieldid = "ACCEPT_SEQ_ID", name = "接收记录ID", logflag = true, propid = "acceptseqid", fieldtype = "BigDecimal")
	private BigDecimal acceptseqid;
	/**
	 * @字段名称：GROUP_MSG_ID
	 * @属性名称：groupmsgid
	 * @字段类型：String
	 * @字段长度：36
	 * @字段描述：群组消息ID
	 */
	@FieldInfo(fieldid = "GROUP_MSG_ID", name = "群组消息ID", logflag = true, propid = "groupmsgid", fieldtype = "String")
	private String groupmsgid;
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
	 * @字段名称：TO_GROUP_ID
	 * @属性名称：togroupid
	 * @字段类型：String
	 * @字段长度：36
	 * @字段描述：接收群组ID
	 */
	@FieldInfo(fieldid = "TO_GROUP_ID", name = "接收群组ID", logflag = true, propid = "togroupid", fieldtype = "String")
	private String togroupid;
	/**
	 * @字段名称：TO_GROUP_NAME
	 * @属性名称：togroupname
	 * @字段类型：String
	 * @字段长度：100
	 * @字段描述：接收群组名称
	 */
	@FieldInfo(fieldid = "TO_GROUP_NAME", name = "接收群组名称", logflag = true, propid = "togroupname", fieldtype = "String")
	private String togroupname;
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
	@FieldInfo(fieldid = "VIEW_STATE", name = "查看状态0未查看1已查看", logflag = true, propid = "viewstate", fieldtype = "String")
	private String viewstate;
	@FieldInfo(fieldid = "VIEW_DATE", name = "查看时间", logflag = true, propid = "viewdate", fieldtype = "Timestamp")
	private Timestamp viewdate;

	public String getViewstate() {
		return viewstate;
	}

	public void setViewstate(String viewstate) {
		this.viewstate = viewstate;
	}

	public Timestamp getViewdate() {
		return viewdate;
	}

	public void setViewdate(Timestamp viewdate) {
		this.viewdate = viewdate;
	}

	public BigDecimal getAcceptseqid() {
		return this.acceptseqid;
	}

	public void setAcceptseqid(BigDecimal acceptseqid) {
		this.acceptseqid = acceptseqid;
	}

	public String getGroupmsgid() {
		return this.groupmsgid;
	}

	public void setGroupmsgid(String groupmsgid) {
		this.groupmsgid = groupmsgid;
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

	public String getTogroupid() {
		return this.togroupid;
	}

	public void setTogroupid(String togroupid) {
		this.togroupid = togroupid;
	}

	public String getTogroupname() {
		return this.togroupname;
	}

	public void setTogroupname(String togroupname) {
		this.togroupname = togroupname;
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



	private JSONObject contentJsonObj;//方便封装json对象的消息体

	public JSONObject getContentJsonObj() {
		return contentJsonObj;
	}

	public void setContentJsonObj(JSONObject contentJsonObj) {
		this.contentJsonObj = contentJsonObj;
	}
}
