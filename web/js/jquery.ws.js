//websocket
var websocket;
//心跳频率
var hbInterval=30000;
//最大重连次数
var maxReconnectInterval=100;
var repeatNum=0;

//websocket地址
var socketaddr="http://im.socket-bd.gz.cegn.cn/imserver/socketjs/websocket";
var wsaddr="ws://im.socket-bd.gz.cegn.cn/imserver/springws/websocket.ws";


var timeFlag=null;
var timedOut = false;
var userid="-7806343473853479463";
var username="杨斌";
var imToken = '-7806343473853479463';
var clienttype="pc";

//协议初始化
function init(){



	if ('WebSocket' in window) {
       /**
		* 创建WebSocket对象时就会到用的后的websocekt服务的afterConnectionEstablished方法
		* 返回{"status":"0","messagetype":"1","server":"server1","message":"OK"}标识连接成功
		*
		* 连接成功之后websocket.send(jsonstr);
		* {"messageid":"3d06dd738c68dce952af70c580186dfc","messagetype":"10","clienttype":"pc","userid":"send"}
		* 发送无密码登陆，后端接收到登陆要求之后认证通过之后将websocetSession对象存起来，这样用户就是在线状态了。
		* 后续每次心跳都重重置一下websocetSession来维持在线状态。
		* 用户退出是要发相应的退出消息清除websocetSession来退出
		*
		*
		*
		* */
		websocket = new WebSocket(wsaddr);
    } else if ('MozWebSocket' in window) {
    	websocket = new MozWebSocket(wsaddr);
    } else {
    	websocket = new SockJS(socketaddr);
    };
    websocket.onopen = function(evnt) {

    };
    websocket.onmessage = function(evnt) {
		var jsonstr=evnt.data;
		console.log(jsonstr);
		obj=$.parseJSON(jsonstr);


		if (obj.messagetype=="1") {//连接成功			
			var obj=new Object();
			obj.messageid=getMessageId();
			obj.messagetype="10";//无密码登陆
            //obj.messagetype="11";//带密码登陆
			obj.clienttype=clienttype;
			obj.userid=userid;
			obj.imToken = imToken;
			var jsonstr=JSON.stringify(obj);
			websocket.send(jsonstr);			
		} else if (obj.messagetype=="12"&&obj.status=='0'){//登录成功，开始启动心跳
			console.log("启动心跳");
			sendHeartBeat();
			//发送消息获取离线信息
            initUnReadMsg(userid,clienttype);

		} else if (obj.messagetype=="3"){//心跳返回
			repeatNum=0;
		} else if (obj.messagetype=="4"){//服务端发送消息
			var html=$("#output").html();
			var html=html+"<br/>"+obj.senddate+"-"+obj.fromname+":"+obj.toname+":"+JSON.stringify(obj.content);
			$("#output").html(html);
			myMessageResponse(obj);
			myMessageViewResponse(obj);

		}else if(obj.messagetype=="25"){
            var html=$("#output").html();
            var html=html+"<br/>"+obj.senddate+"-"+obj.fromname+":"+obj.toname+":"+JSON.stringify(obj.content);
            $("#output").html(html);
            myGroupMessageResponse(obj);
            myGroupMessageViewResponse(obj)//发送消息查看包括-----具体开发中应该有客户看到消息之后发出消息查看报告
		}

		else if(obj.messagetype=="5"){//消息送达报告
			console.log("收到消息送达报告："+JSON.stringify(obj));
		}
	};
	websocket.onerror = function(evnt) {
		sendHeartBeat();
	};
	websocket.onclose = function(evnt) {
		
		//init();
	};
}


/**
 * 消息送达反馈----单聊消息
 * @param messageid
 * @param fromid
 * @param fromname
 */
function myMessageResponse(msgObj){
    var obj=new Object();
    obj.messageid=msgObj.messageid;
    obj.messagetype="5";
    obj.clienttype=msgObj.clienttype;
    obj.fromid=msgObj.toid;
    obj.fromname=msgObj.toname;
    obj.toid=msgObj.fromid;
    obj.toname=msgObj.fromname;
    obj.acceptdate=getCurTime();
    var jsonstr=JSON.stringify(obj);
    websocket.send(jsonstr);
}

/**
 * 消息送达反馈----群聊消息
 * @param messageid
 * @param fromid
 * @param fromname
 */
function myGroupMessageResponse(msgObj){
    var obj=new Object();
    obj.groupmsgid=msgObj.groupmsgid;
    obj.messagetype="22";
    obj.clienttype=msgObj.clienttype;
    obj.fromid=msgObj.toid;
    obj.fromname=msgObj.toname;
    obj.toid=msgObj.fromid;
    obj.toname=msgObj.fromname;
    obj.acceptdate=getCurTime();
    var jsonstr=JSON.stringify(obj);
    websocket.send(jsonstr);
}



/**
 * 消息查看反馈---单聊消息
 * @param messageid
 * @param fromid
 * @param fromname
 */
function myMessageViewResponse(msgObj){
    var obj=new Object();
    obj.messageid=msgObj.messageid;
    obj.messagetype="14";
    obj.clienttype=msgObj.clienttype;
    obj.fromid=msgObj.toid;
    obj.fromname=msgObj.toname;
    obj.toid=msgObj.fromid;
    obj.toname=msgObj.fromname;
    obj.viewdate=getCurTime();
    var jsonstr=JSON.stringify(obj);
    websocket.send(jsonstr);
}

/**
 * 消息查看反馈---群聊消息
 * @param messageid
 * @param fromid
 * @param fromname
 */
function myGroupMessageViewResponse(msgObj){
    var obj=new Object();
    obj.groupmsgid=msgObj.groupmsgid;
    obj.messagetype="23";
    obj.clienttype=msgObj.clienttype;
    obj.fromid=msgObj.toid;
    obj.fromname=msgObj.toname;
    obj.toid=msgObj.fromid;
    obj.toname=msgObj.fromname;
    obj.viewdate=getCurTime();
    var jsonstr=JSON.stringify(obj);
    websocket.send(jsonstr);
}


function initUnReadMsg(userid,clienttype){
    var obj=new Object();
    obj.userid=userid;
    obj.messagetype="90";
    obj.clienttype=clienttype;
    obj.acceptdate=getCurTime();
    var jsonstr=JSON.stringify(obj);
    websocket.send(jsonstr);


}


/**
 * 群组消息发送
 * @param text
 * @param toid
 * @param toname
 */
function sendGroupMessage(text,togroupid,togroupname){


	var obj=new Object();
	obj.messageid=getMessageId();
	obj.messagetype="21";
	obj.clienttype=clienttype;
	obj.fromid=userid;
	obj.fromname=username;
	obj.senddate=getCurTime();
	obj.togroupid = togroupid;//
	obj.togroupname =togroupname;

	//lly添加
   var msgContent = new Object();
   msgContent.businessType = "grouppointtopoint";
   msgContent.viewContent={
        "viewType":"text",
		"content": text
   };
   obj.content = msgContent;
   var jsonstr=JSON.stringify(obj);
	websocket.send(jsonstr);
}

/**
 * 消息发送
 * @param text
 * @param toid
 * @param toname
 */
function sendMessage(text,toid,toname){


    var obj=new Object();
    obj.messageid=getMessageId();
    obj.messagetype="4";
    obj.clienttype=clienttype;
    obj.fromid=userid;
    obj.fromname=username;
    obj.toid=toid;
    obj.toname=toname;
    obj.senddate=getCurTime();

    var content = new Object();
    content.businessType = 'pointtopoint';
    var viewContent=new Object();
    viewContent.viewType="text"
    viewContent.content = text;
    content.viewContent = viewContent;
    obj.content = content;

    //lly添加
    var jsonstr=JSON.stringify(obj);
    console.log("发送的字符串"+jsonstr)
    websocket.send(jsonstr);

    $("#lastsendMsgId").val(obj.messageid);
}



//获取消息ID
function getMessageId(){
	var guid = "";
    for (var i = 1; i <= 32; i++){
      	var n = Math.floor(Math.random()*16.0).toString(16);
      	guid +=   n;
      	if((i==8)||(i==12)||(i==16)||(i==20))
        	guid += "";
    }
	return guid;    
}
function getCurTime() {
	var now = new Date();
	var year=""+now.getFullYear();
	var month=now.getMonth()+1;
	if(month<10){month="0"+month;}
	var day = now.getDate();
	if(day<10){day = "0"+day;}
	var hours = now.getHours();
	if(hours<10){hours = "0"+hours;}
	var minutes = now.getMinutes();
	if(minutes<10){minutes = "0"+minutes;}
	var seconds = now.getSeconds();
	if(seconds<10){seconds = "0"+seconds;}
	return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":"+ seconds;
}
//清空心跳
function clearHeartBeat(){
	if (timeFlag!=null){
		clearInterval(timeFlag);
		timeFlag=null;
	}
}
//发送心跳
function sendHeartBeat(){
	if (timeFlag!=null){
		clearInterval(timeFlag);
		timeFlag=null;
	}
	timeFlag=setInterval(function() {
		repeatNum++;
		if (repeatNum<=maxReconnectInterval){//小于重试次数才发送心跳，超过后停止心跳发送
			console.log("发送心跳");
			var obj=new Object();
			obj.messageid=getMessageId;
			obj.userid=userid;
			obj.clienttype=clienttype;
			obj.messagetype="2";
			var jsonstr=JSON.stringify(obj);
			websocket.send(jsonstr);
		} else{
			if (timeFlag!=null){
				clearInterval(timeFlag);
				timeFlag=null;
			}
		}
		
	},hbInterval);
}

// 关闭socekt

function closeSocket(){
	if(websocket){
        websocket.close()
        //websocket = null;
	}

}
// 撤回消息

function revokeMsg(){
    var obj=new Object();
    obj.messageid= $("#lastsendMsgId").val();
    obj.messagetype="30";
    obj.clienttype=clienttype;
    var fromid="-7806343473853479463";
    var fromname="杨斌";
    var toid="5604596590712865886";
    var toname="潘仁海";

    if (flag=="2"){
        toid="-7806343473853479463";
        toname="杨斌";
        fromid="5604596590712865886";
        fromname="潘仁海";
    }

    obj.fromid=fromid;
    obj.fromname=fromname;
    obj.toid=toid;
    obj.toname=toname;
    obj.revokemsgtype = 'grouppointtopoint';
    websocket.send(JSON.stringify(obj));

}