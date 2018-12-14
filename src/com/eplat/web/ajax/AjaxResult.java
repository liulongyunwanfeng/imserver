package com.eplat.web.ajax;



/**
 * 
 * 
 * @项目名称：eplatweb
 * @类名称：AjaxResult
 * @类描述：ajax 的返回结果
 * @创建人：高洋
 * @创建时间：2011-5-7 下午02:34:21
 * @修改人：高洋
 * @修改时间：2011-5-7 下午02:34:21
 * @修改备注：
 * @version 1.0.0.1
 * 
 */
public class AjaxResult implements java.io.Serializable {

	/**
	 * @属性名称：serialVersionUID
	 * @属性类型：long
	 * @属性描述：
	 */
	private static final long serialVersionUID = 1L;
	private String clientId;
	private String showErrors = "";

	private Object result = null;// 执行结果
	private int statusCode = 0;// 返回的状态编码
	private String statusMessage;// 返回的状态信息
	private String noticeid;//通知ID

	public String getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}

	
	public String getShowErrors() {
		return showErrors;
	}

	public void setShowErrors(String showErrors) {
		this.showErrors = showErrors;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}
