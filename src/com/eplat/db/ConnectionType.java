package com.eplat.db;

/**
 * 连接类型常量类
 * @author Administrator
 *
 */
public final class ConnectionType {
	/**
	 * jdbc 数据连接
	 */
	public static final String CONNECT_TYPE_JDBC="jdbc";
	/**
	 * 使用proxool连接池进行
	 */
	public static final String CONNECT_TYPE_PROXOOL="proxool";
	/**
	 * 使用C3P0连接池
	 */
	public static final String CONNECT_TYPE_C3P0="c3p0";
	/**
	 * 使用容器连接池
	 */
	public static final String CONNECT_JNDI="jndi";

	public static final String CONNECT_DRUID ="druid";
}
