package com.eplat.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.eplat.utils.LoggerUtils;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 数据库连接信息配置类
 * 
 * @author Administrator
 * 
 */
public class Configuration {
	/**
	 * 主要是为了兼容Tomcat的JNDI连接
	 */
	public static final String CONTAINER_PREFIX = "java:comp/env/";
	/**
	 * 数据源类
	 */
	private DataSource dataSource;
	/**
	 * 数据库连接类型
	 */
	private String connectionType;
	/**
	 * 数据库名称
	 */
	private String dataBaseName;
	/**
	 * 数据库类型
	 */
	private String dataBaseType;
	private Hashtable<String, String> assistList = new Hashtable<String, String>();

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	public String getDataBaseType() {
		return dataBaseType;
	}

	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	public Hashtable<String, String> getAssistList() {
		return assistList;
	}

	public void setAssistList(Hashtable<String, String> assistList) {
		this.assistList = assistList;
	}

	/**
	 * 设置数据库连接的辅助信息
	 * 
	 * @param key
	 * @param value
	 */
	public void setAssistInfo(String key, String value) {
		LoggerUtils.debug("DB", String.format(
				"read database config paramid：{},paramvalue：{}", key, value));
		if ("connection-type".equalsIgnoreCase(key)) {
			this.connectionType = value;
		} else if ("dbtype".equalsIgnoreCase(key)) {
			this.dataBaseType = value;
		} else if ("dbname".equalsIgnoreCase(key)) {
			this.dataBaseName = value;
		} else {
			this.assistList.put(key, value);
		}
	}

	/**
	 * 获取数据库连接的辅助信息
	 * 
	 * @param key
	 * @return
	 */
	public String getAssistInfo(String key) {
		if ("connection-type".equalsIgnoreCase(key)) {
			return this.connectionType;
		} else if ("dbtype".equalsIgnoreCase(key)) {
			return this.dataBaseType;
		} else if ("dbname".equalsIgnoreCase(key)) {
			return this.dataBaseName;
		} else {
			return this.assistList.get(key);
		}
	}

	/**
	 * 根据配置初始化数据源
	 * 
	 * @throws DBException
	 */
	public void initDataSource() throws Exception {
		if (this.assistList.size() == 0) {
			throw new Exception("数据库连接配置为空，不能连接数据库");
		} else if (ConnectionType.CONNECT_TYPE_JDBC
				.equalsIgnoreCase(this.connectionType)) {// jdbc 数据库连接
			this.createJdbcDataSource();
		} else if (ConnectionType.CONNECT_TYPE_PROXOOL
				.equalsIgnoreCase(this.connectionType)) {// Proxool 数据库连接
			this.createProxoolDataSource();
		} else if (ConnectionType.CONNECT_JNDI
				.equalsIgnoreCase(this.connectionType)) {
			this.createJndiDataSource();

		} else if(ConnectionType.CONNECT_DRUID
				.equalsIgnoreCase(this.connectionType)){
			this.createDridDataSource();
		}
		else {
			throw new Exception("不支持的连接类型，连接类型只支持【proxool、jndi、cp30、jdbc】");
		}
	}

	/**
	 * 创建JDBC 数据库连接
	 * 
	 * @throws DBException
	 */
	public void createJdbcDataSource() throws Exception {
		JdbcDataSource jdbcsource = new JdbcDataSource();
		jdbcsource.setDriverClass(this.assistList.get("driverclass"));
		jdbcsource.setUrl(assistList.get("url"));
		jdbcsource.setUid(assistList.get("uid"));
		jdbcsource.setPwd(assistList.get("pwd"));
		this.dataSource = jdbcsource;
	}

	/**
	 * 创建Proxool连接池
	 * 
	 * @throws DBException
	 */
	public void createProxoolDataSource() throws Exception {
		ProxoolDataSource ds = new ProxoolDataSource();
		ds.setAlias(assistList.get("dbid"));
		
		ds.setUrl(assistList.get("url"));
		ds.setDriverClass(assistList.get("driverclass"));
		ds.setUid(assistList.get("uid"));
		ds.setPwd(assistList.get("pwd"));
		ds.setHousekeepingsleeptime(assistList.get("housekeepingsleeptime"));
		ds.setHousekeepingtestsql(assistList.get("housekeepingtestsql"));
		ds.setMaximumconnectioncount(assistList.get("maximumconnectioncount"));
		ds.setMinimumconnectioncount(assistList.get("minimumconnectioncount"));
		ds.setMaximumconnectionlifetime(assistList
				.get("maximumconnectionlifetime"));
		ds.setSimultaneousbuildthrottle(assistList
				.get("simultaneousbuildthrottle"));
		ds.setRecentlystartedthreshold(assistList
				.get("recentlystartedthreshold"));
		ds.setOverloadwithoutrefusallifetime(assistList
				.get("overloadwithoutrefusallifetime"));
		ds.setMaximumactivetime(assistList.get("maximumactivetime"));
		ds.configure();
		this.dataSource = ds;
	}

	/**
	 * 创建JNDI数据连接
	 * 
	 * @throws DBException
	 */
	public void createJndiDataSource() throws Exception {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put("java.naming.factory.initial", assistList.get("initial"));
			env.put("java.naming.provider.url", assistList.get("url"));
			DirContext ctx = new InitialDirContext(env);
			String jndiName = assistList.get("jndi");
			try {// 兼容tomcat等容器
				this.dataSource = (DataSource) ctx.lookup(CONTAINER_PREFIX
						+ jndiName);
			} catch (Exception e) {
				try {// 使用weblogic等容器
					this.dataSource = (DataSource) ctx.lookup(jndiName);
				} catch (Exception e1) {
					throw new Exception(e);
				}
			}
		} catch (NamingException e) {
			throw new Exception(e);
		}
	}



	/**
	 * 创建DRUID数据连接
	 * @throws DBException
	 */
	public void createDridDataSource() throws Exception {

		DruidDataSource ds = new DruidDataSource();
		ds.setUrl(assistList.get("url"));
		ds.setUsername(assistList.get("uid"));
		ds.setPassword(assistList.get("pwd"));
		ds.setFilters(assistList.get("filters"));
		ds.setMaxActive(new Integer(assistList.get("maxactive")));
		ds.setInitialSize(new Integer(assistList.get("initialsize")));
		//ds.setMaxWait(new Long(assistList.get("maxwait")));
		ds.setMinIdle(new Integer(assistList.get("minidle")));
		ds.setTimeBetweenEvictionRunsMillis(new Long(assistList.get("timebetweenevictionrunsmillis")));
		ds.setMinEvictableIdleTimeMillis(new Long(assistList.get("minevictableidletimemillis")));
		ds.setTestWhileIdle(new Boolean(assistList.get("testwhileidle")));
		ds.setTestOnBorrow(new Boolean(assistList.get("testonborrow")));
		ds.setTestOnReturn(new Boolean(assistList.get("testonreturn")));
		ds.setPoolPreparedStatements(new Boolean(assistList.get("poolpreparedstatements")));
		ds.setMaxOpenPreparedStatements(new Integer(assistList.get("maxopenpreparedstatements")));
		ds.setValidationQuery(assistList.get("validationquery"));
		ds.setRemoveAbandoned(new Boolean(assistList.get("removeabandoned")));
		ds.setRemoveAbandonedTimeout(new Integer(assistList.get("removeabandonedtimeout")));
		ds.setLogAbandoned(new Boolean(assistList.get("logabandoned")));
		ds.setDefaultTransactionIsolation(new Integer(assistList.get("defaulttransactionisolation")));

		List<String> connectioninitsqls = new ArrayList<>();
		connectioninitsqls.add(assistList.get("connectioninitsqls"));
		ds.setConnectionInitSqls(connectioninitsqls);
		this.dataSource=ds;
	}
}
