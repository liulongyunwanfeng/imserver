package com.eplat.im.domain;

import com.eplat.annotation.FieldInfo;
import com.eplat.annotation.TableInfo;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @类名称：GroupsendBean
 * @类描述：群组发送
 * @创建人：$author
 * @创建时间：2017-09-22 22:24:53
 */
@TableInfo(tablename = "IM_GROUP_SEND", tabledesc = "群组发送", logtype = "default")
public class GroupsendBean extends ImPacket {
	/**
	 * @字段名称：GROUP_SEQ_ID
	 * @属性名称：groupseqid
	 * @字段类型：BigDecimal
	 * @字段长度：19
	 * @字段描述：群组消息流水号
	 */
	@FieldInfo(fieldid = "GROUP_SEQ_ID", name = "群组消息流水号", logflag = true, propid = "groupseqid", fieldtype = "BigDecimal")
	private BigDecimal groupseqid;
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
	 * @字段名称：GROUP_NUM
	 * @属性名称：groupnum
	 * @字段类型：int
	 * @字段长度：10
	 * @字段描述：群组人数
	 */
	@FieldInfo(fieldid = "GROUP_NUM", name = "群组人数", logflag = true, propid = "groupnum", fieldtype = "int")
	private int groupnum;
	/**
	 * @字段名称：VIEW_NUM
	 * @属性名称：viewnum
	 * @字段类型：int
	 * @字段长度：10
	 * @字段描述：查看人数
	 */
	@FieldInfo(fieldid = "VIEW_NUM", name = "查看人数", logflag = true, propid = "viewnum", fieldtype = "int")
	private int viewnum;
	/**
	 * @字段名称：ACCEPT_NUM
	 * @属性名称：acceptnum
	 * @字段类型：int
	 * @字段长度：10
	 * @字段描述：接收人数
	 */
	@FieldInfo(fieldid = "ACCEPT_NUM", name = "接收人数", logflag = true, propid = "acceptnum", fieldtype = "int")
	private int acceptnum;

	public int getViewnum() {
		return viewnum;
	}

	public void setViewnum(int viewnum) {
		this.viewnum = viewnum;
	}

	public BigDecimal getGroupseqid() {
		return this.groupseqid;
	}

	public void setGroupseqid(BigDecimal groupseqid) {
		this.groupseqid = groupseqid;
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

	public int getGroupnum() {
		return this.groupnum;
	}

	public void setGroupnum(int groupnum) {
		this.groupnum = groupnum;
	}

	public int getAcceptnum() {
		return this.acceptnum;
	}

	public void setAcceptnum(int acceptnum) {
		this.acceptnum = acceptnum;
	}


	private JSONObject contentJsonObj;//方便封装json对象的消息体

	public JSONObject getContentJsonObj() {
		return contentJsonObj;
	}

	public void setContentJsonObj(JSONObject contentJsonObj) {
		this.contentJsonObj = contentJsonObj;
	}
}
