package com.eplat.im;

/**
 * Created by liulongyun on 2018/7/26.
 */
public class CommonConstants {

    //通信token存储key前缀
    public  static final String IM_TOKEN_KEY_PREFIX="IM_TOKEN_KEY_";//通讯票据Key前缀

    //
    public  static String  OPTION_RESULT_SUCCESS="0";//操作成功
    public  static String  OPTION_RESULT_FAIL="-1";//操作成功


    //businessType常量grouppointtopoint   pointtopoint


    public  static String  IM_BUSINESSTYPE_POINTTOPOINT="pointtopoint";//点对点聊天
    public  static String  IM_BUSINESSTYPE_GROUPPOINTTOPOINT="grouppointtopoint";//群聊天





    //用户默认头像
    public static  String HEADIMG_DEFAULT = "im/head/headimage_default.jpg";
    public static  String HRADIMGURLFIX = "http://huizhi-mhdemo.oss-intranet.gzdata.gov.cn/";

    public static String IM_USERHEADIMG_CATCH_PRIFEX="IM_USERHEADIMG_";
    public static int IM_USERHEADIMG_CATCH_TIMEOUT=7*24*60*60;//7天





    public  static  String getDefautHeadImg(){
        return  HEADIMG_DEFAULT+HRADIMGURLFIX;
    }





    //通信token存储key前缀
    public  static final String IM_MSG_FROMUSER_HEADIMGANDIMSTATUS="IM_MSG_FROMUSER_HEADIMG";//头像，工作状态

    public  static final String IM_STATUS_WORKING="工作中";


    //撤回时段

    public static Long REVOKE_TIME=2*60*1000l;//三分钟

    //系统消息提醒类型
    public  static final String MSG_VIEW_TYPE_SERVICESYSTEM = "serviceSystem";

    public  static String  IM_CLIENT_TYPE_PC="pc";//pc端
    public  static String  IM_CLIENT_TYPE_MOBILE="mobile";//移动端








    //队列绑定的key名称后缀常量定义
    public  static final String QUEUE_SUFFIX_NAME_PTPMSG="ptpmsg";//单聊队列
    public  static final String QUEUE_SUFFIX_NAME_GROUP="group";//群聊队列
    public  static final String QUEUE_SUFFIX_NAME_RESPTEXT="resptext";//消息相应队列
    public  static final String QUEUE_SUFFIX_NAME_OFFLINETEXT="offlinetext";//离线消息队列
    public  static final String QUEUE_SUFFIX_NAME_REVOKETEXT="revoketext";//消息撤回队列













}
