package com.eplat.db;

/**
 * 
 * @项目名称：eplat
 * @类名称：DBException
 * @类描述：数据库异常描述
 * @创建人：高洋
 * @创建时间：2010-2-6 下午04:53:50
 * @修改人：高洋
 * @修改时间：2010-2-6 下午04:53:50
 * @修改备注：
 * @version
 */
public class DBException extends RuntimeException {
	/**
	 * 完整跟踪信息
	 */
	public static int FULL_STACKTRACE = 1;
	/**
	 * 返回错误代码和错误消息
	 */
	public static int CODE_MESSAGE = 2;

	private static final long serialVersionUID = 1L;
	private int m_errorCode = 0;

	public DBException() {
		super();
	}

	/**
	 * 错误消息
	 * 
	 * @param errorMsg
	 */
	public DBException(String errorMsg) {
		super(errorMsg);
	}

	/**
	 * 错误代码
	 * 
	 * @param errorCode
	 * @param errorMsg
	 */
	public DBException(int errorCode, String errorMsg) {
		super(errorMsg);
		this.m_errorCode = errorCode;

	}

	/**
	 * 错误消息 异常
	 * 
	 * @param errorMsg
	 * @param throwable
	 */
	public DBException(String errorMsg, Throwable throwable) {
		super(errorMsg, throwable);
	}

	public DBException(int errorCode, Throwable throwable) {
		super(throwable);
		this.m_errorCode = errorCode;
	}

	public DBException(int errorCode, String errorMsg, Throwable throwable) {
		super(errorMsg, throwable);
		this.m_errorCode = errorCode;
	}

	public DBException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * 错误错误代码
	 * 
	 * @return
	 */
	public int getErrorCode() {
		return this.m_errorCode;
	}

	/**
	 * 获取错误代码和错误消息
	 * 
	 * @return
	 */
	public String getCodeMsg() {
		return String.format("error code %d,error message %s",
				this.m_errorCode, this.getMessage());

	}

	/**
	 * 获取错误消息堆栈
	 * 
	 * @return
	 */
	public String getStackMsg() {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackArray = this.getStackTrace();
		for (int i = 0; i < stackArray.length; i++) {
			StackTraceElement element = stackArray[i];
			sb.append(element.toString() + "\n");
		}
		return sb.toString();
	}
}
