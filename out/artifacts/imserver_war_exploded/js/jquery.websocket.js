function sendHeartBeat(id,userid,websocket){
	var obj=new Object();
	obj.messageid=id;
	obj.userid=userid;
	obj.messagetype="2";
	var jsonstr=JSON.stringify(obj);
	websocket.send(jsonstr);
}
$.fn.imwebsocket=function(opts){
	var defaults={
		userid:'',//用户ID
		wsaddr:'ws://localhost:808/imserver/springws/websocket.ws',//websocket
		sockaddr:'http://localhost:808/imserver/socketjs/websocket',//socketjs的地址
		heartbeat:5000,//心跳的间隔时间
		onOpen:function(event) {},//连接成功触发
	    onMessage:function(event) {},//消息触发
	    onError:function(event) {},//异常触发
	    onClose:function(event) {} //连接关闭
	}
	this.options = $.extend(defaults,opts);
	this.websocket=null;
	this.intervalFlag=null;
	var _this=this;
	this.init=function(){
		if ('WebSocket' in window) {
			this.websocket = new WebSocket(this.options.wsaddr);
	    } else if ('MozWebSocket' in window) {
	    	this.websocket = new MozWebSocket(this.options.wsaddr);
	    } else {
	    	this.websocket = new SockJS(this.options.sockaddr);
	    };
	    this.websocket.onopen = function(evnt) {
			_this.options.onOpen(evnt);
			
		};
		this.websocket.onmessage = function(evnt) {
			var jsonstr=evnt.data;
			obj=$.parseJSON(jsonstr);
			if (obj.messagetype=="1") {//连接成功
				if(_this.options.userid!=""){
					var obj=new Object();
					obj.messageid=_this.getid();
					obj.messagetype="10";
					obj.userid=_this.options.userid;
					var jsonstr=JSON.stringify(obj);
					_this.websocket.send(jsonstr);
				}
			} else if (obj.messagetype=="12"){//登录成功，开始启动心跳
				if (_this.options.heartbeat!=-1){//启动心跳
					
					_this.intervalFlag=setInterval(sendHeartBeat,_this.options.heartbeat,1,2,_this.websocket);//(,);
				};

			}
			_this.options.onMessage(evnt);
			
		};
		this.websocket.onerror = function(evnt) {
			_this.options.onError(evnt);
		};
		this.websocket.onclose = function(evnt) {
			_this.options.onClose(evnt);
		};
	},
	this.getid=function(){
		var guid = "";
	    for (var i = 1; i <= 32; i++){
	      	var n = Math.floor(Math.random()*16.0).toString(16);
	      	guid +=   n;
	      	if((i==8)||(i==12)||(i==16)||(i==20))
	        	guid += "";
	    }
   		return guid;    
	},
	/*
	 * 发送心跳指令
	 */
	this.disconnect=function(){
		clearInterval(this.intervalFlag);
	};
	this.init();
	return this;
}