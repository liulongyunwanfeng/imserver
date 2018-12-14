package com.eplat.db;

import com.eplat.utils.FileUtils;
import com.eplat.utils.LoggerUtils;
import com.eplat.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * 数据库连接管理类
 * 
 * @author Administrator
 * 
 */
public class ConnectionManager {
	// 连接配置文件的前缀
	public static String CONF_PREFIX = "/db_connection";
	private static Hashtable<String, Configuration> pools = new Hashtable<String, Configuration>();
	private static ThreadLocal<DBConnections> connectionThreadLocal = new ThreadLocal<DBConnections>();




	public static String getDataBaseType(String dbid) throws DBException {
		if (!StringUtils.hasLength(dbid)) {
			dbid = "default";
		}
		Configuration configuration = (Configuration) pools.get(dbid);
		if (configuration == null) {

			configuration = ConfigurationUtils
					.loadConfig(getInputStream(CONF_PREFIX + "_" + dbid
							+ ".xml"));
			pools.put(dbid, configuration);
		}
		String dbType = String.valueOf(configuration.getDataBaseType());
		return dbType;
	}

	public static InputStream getInputStream(String fileName)
			throws DBException {
		InputStream in = ConnectionManager.class.getResourceAsStream(fileName);
		try {
			if (in == null) {
				String pathName = System.getProperty("user.dir");
				if (!pathName.endsWith("/")) {
					pathName = pathName + "/";
				}
				pathName = pathName + fileName;

				if (FileUtils.isExist(pathName)) {
					in = new FileInputStream(new File(pathName));
				}
			}
		} catch (FileNotFoundException e) {
			throw new DBException(e);
		}
		return in;
	}

	/**
	 * 获取数据连接
	 * 
	 * @return
	 * @throws DBException
	 */
	public static DBConnection getConnection() throws Exception {
		DBConnection dbConnection = null;
		if (connectionThreadLocal.get() != null) {
			DBConnections connections = connectionThreadLocal.get();
			dbConnection = connections.getConnection("default");
		}
		if (dbConnection == null) {
			Configuration configuration = (Configuration) pools.get("default");
			if (configuration == null) {
				configuration = ConfigurationUtils
						.loadConfig(getInputStream(CONF_PREFIX + "_default.xml"));
				pools.put("default", configuration);
				LoggerUtils.debug("DB","从配置文件中成功加载数据库配置");
			}
			dbConnection = createDBConnection(configuration);
			if (connectionThreadLocal.get() == null) {
				connectionThreadLocal.set(new DBConnections());
			}
			connectionThreadLocal.get().setConnection(dbConnection);
		}
		return dbConnection;
	}

	/**
	 * 获取链接不放入threadlocal
	 * 
	 * @return
	 * @throws DBException
	 */
	public static DBConnection getNoThreadConnection(String dbid)
			throws Exception {
		DBConnection dbConnection = null;
		Configuration configuration = (Configuration) pools.get(dbid);
		if (configuration == null) {
			configuration = ConfigurationUtils
					.loadConfig(getInputStream(CONF_PREFIX + "_" + dbid
							+ ".xml"));
			pools.put(dbid, configuration);
			LoggerUtils.debug("DB","从配置文件中成功加载数据库配置");
		}
		dbConnection = createDBConnection(configuration);

		return dbConnection;
	}

	/**
	 * 从本地线程中获取连接
	 * 
	 * @param dbid
	 * @return
	 * @throws DBException
	 */
	public static DBConnection getTreadLocalConnection(String dbid)
			throws Exception {
		DBConnections connections = connectionThreadLocal.get();
		if (connections != null) {
			if (StringUtils.hasLength(dbid)) {
				return connections.getConnection(dbid);
			} else {
				return connections.getConnection("default");
			}
		} else {
			return null;
		}
	}

	public static DBConnection createDBConnection(Configuration configuration)
			throws Exception {
		String dbType = configuration.getDataBaseType();
		DBConnection dbConnection = null;
		LoggerUtils.debug("DB",String.format("准备创建数据库类型为：【{}】的数据连接", configuration.getDataBaseType()));
		if ((dbType != null)
				&& (dbType.equalsIgnoreCase(DataBaseType.DB_TYPE_SQL))) {// sql2000及以下版本
			dbConnection = new SqlConnection();
			dbConnection.setConfiguration(configuration);
			dbConnection.getConnection();
		} else if ((dbType != null)
				&& (dbType.equalsIgnoreCase(DataBaseType.DB_TYPE_SQL2005))) {// sqlserver2005及以上版本
			dbConnection = new Sql2005Connection();
			dbConnection.setConfiguration(configuration);
			dbConnection.getConnection();
		}else if ((dbType != null)
				&& (dbType.equalsIgnoreCase(DataBaseType.DB_TYPE_ORACLE))) {// sqlserver2005及以上版本
			dbConnection = new OracleConnection();
			dbConnection.setConfiguration(configuration);
			dbConnection.getConnection();
		}else if ((dbType != null)
				&& (dbType.equalsIgnoreCase(DataBaseType.DB_TYPE_MYSQL))) {// sqlserver2005及以上版本
			dbConnection = new MySqlConnection();
			dbConnection.setConfiguration(configuration);
			dbConnection.getConnection();
		} else {
			throw new DBException("不支持设置的数据库类型！");
		}
		return dbConnection;
	}

	/**
	 * 关闭所有数据库连接
	 */
	public static void closeConnections() throws Exception {
		if (connectionThreadLocal.get() != null) {
			DBConnections connections = connectionThreadLocal.get();
			connections.clear();
			connections=null;
		}
		connectionThreadLocal.remove();
		connectionThreadLocal.set(null);
		System.gc();

	}

	/**
	 * 根据连接对象ID来获取连接
	 * 
	 * @param dbid
	 * @return
	 * @throws DBException
	 */
	public static synchronized DBConnection getConnection(String dbid)
			throws Exception {
		DBConnection dbConnection = null;
		if (connectionThreadLocal.get() != null) {
			DBConnections connections = connectionThreadLocal.get();
			dbConnection = connections.getConnection(dbid);
		}
		if (dbConnection == null) {
			//
			//System.out.println("创建数据库连接");
			Configuration dbConfig = (Configuration) pools.get(dbid);
			if (dbConfig == null) {
				dbConfig = ConfigurationUtils
						.loadConfig(getInputStream(CONF_PREFIX + "_" + dbid
								+ ".xml"));
				pools.put(dbid, dbConfig);
			}
			dbConnection = createDBConnection(dbConfig);
			if (connectionThreadLocal.get() == null) {
				connectionThreadLocal.set(new DBConnections());
			}
			connectionThreadLocal.get().setConnection(dbConnection);
		}
		return dbConnection;
	}
}
