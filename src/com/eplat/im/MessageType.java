package com.eplat.im;

public class MessageType {
	/**
	 * 连接成功
     *
	 */
	public static String CONNECTED="1";
	//心跳
	public static String HEART_BEAT="2";
	//心跳结果
	public static String HEART_BEAT_RESULT="3";
	//文本消息
	public static String TEXT="4";
	//文本消息送达报告
	public static String TEXT_RESULT="5";
	//文本消息已发送达报告----add by lly
	public static String TEXT_HAS_SEND="105";
	//群文本消息已发送达报告----add by lly
	public static String GROUP_TEXT_HAS_SEND="106";


	//核心组件产生的系统提醒消息----add by lly
	public static String MSG_TYPE_IM_NOTICE="107";

	//查看数据
	public static String VIEW_TEXT="14";
	//查看数据返回
	public static String VIEW_TEXT_RESULT="15";
	//图片消息
	public static String IMAGE="6";
	//图片结果
	public static String IMAGE_RESULT="7";
	//文件消息
	public static String FILE="8";
	//文件消息结果
	public static String FILE_RESULT="9";
	//无密码登录，传入用户id
	public static String LOGIN_NOPWD="10";
	//需要密码登录
	public static String LOGIN_PWD="11";
	//登录结果
	public static String LOGIN_RESULT="12";
	//退出
	public static String LOGIN_OUT="98";

	//退出结果
	public static String LOGIN_OUT_RESULT="101";

	//错误消息
	public static String ERROR_MSG="99";
	//初始化消息
	public static String INIT_MSG="90";
	//初始化消息
	public static String INIT_MSG_RESULT="91";
	//群组消息
	public static String GROUP_TXT="21";
	//群组消息到用户
	public static String GROUP_TXT_USER="25";
	//群组消息结果
	public static String GROUP_TXT_RESULT="22";

	public static String GROUP_TXT_VIEW="23";
	//群组消息查看结果
	public static String GROUP_TXT_VIEW_RESULT="24";
	
	//超过5分钟认为超时了
	public static long MAX_TIME=1000*60*5;

	// 撤回消息的消息类型
	public static String MESSAGE_TYPE_REVOKE="30";



	
	
}
