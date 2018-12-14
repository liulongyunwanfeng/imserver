package com.eplat.db;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

;
/**
 * 
 * @项目名称：eplat
 * @类名称：ProxoolDataSource
 * @类描述：Proxool 连接池的数据源
 * @创建人：高洋
 * @创建时间：2010-2-6 下午05:42:31
 * @修改人：高洋
 * @修改时间：2010-2-6 下午05:42:31
 * @修改备注：
 * @version
 */
public class ProxoolDataSource implements DataSource {
	private Properties props = new Properties();
	private String alias;
	private String url;
	private String driverClass;
	private String uid;
	private String pwd;
	private String housekeepingsleeptime;
	private String housekeepingtestsql;
	private String maximumconnectioncount;
	private String minimumconnectioncount;
	private String maximumconnectionlifetime;
	private String simultaneousbuildthrottle;
	private String recentlystartedthreshold;
	private String overloadwithoutrefusallifetime;
	private String maximumactivetime;

	private String prototypecount;
	private boolean isConfigure = false;

	public ProxoolDataSource() {

	}

	/**
	 * 
	 * configure
	 * 
	 * @描述：启动proxool的配置
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public void configure() throws DBException {
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getHousekeepingsleeptime() {
		return housekeepingsleeptime;
	}

	public void setHousekeepingsleeptime(String housekeepingsleeptime) {
		this.housekeepingsleeptime = housekeepingsleeptime;
	}

	public String getHousekeepingtestsql() {
		return housekeepingtestsql;
	}

	public void setHousekeepingtestsql(String housekeepingtestsql) {
		this.housekeepingtestsql = housekeepingtestsql;
	}

	public String getMaximumconnectioncount() {
		return maximumconnectioncount;
	}

	public void setMaximumconnectioncount(String maximumconnectioncount) {
		this.maximumconnectioncount = maximumconnectioncount;
	}

	public String getMinimumconnectioncount() {
		return minimumconnectioncount;
	}

	public void setMinimumconnectioncount(String minimumconnectioncount) {
		this.minimumconnectioncount = minimumconnectioncount;
	}

	public String getMaximumconnectionlifetime() {
		return maximumconnectionlifetime;
	}

	public void setMaximumconnectionlifetime(String maximumconnectionlifetime) {
		this.maximumconnectionlifetime = maximumconnectionlifetime;
	}

	public String getSimultaneousbuildthrottle() {
		return simultaneousbuildthrottle;
	}

	public void setSimultaneousbuildthrottle(String simultaneousbuildthrottle) {
		this.simultaneousbuildthrottle = simultaneousbuildthrottle;
	}

	public String getRecentlystartedthreshold() {
		return recentlystartedthreshold;
	}

	public void setRecentlystartedthreshold(String recentlystartedthreshold) {
		this.recentlystartedthreshold = recentlystartedthreshold;
	}

	public String getOverloadwithoutrefusallifetime() {
		return overloadwithoutrefusallifetime;
	}

	public void setOverloadwithoutrefusallifetime(
			String overloadwithoutrefusallifetime) {
		this.overloadwithoutrefusallifetime = overloadwithoutrefusallifetime;
	}

	public String getMaximumactivetime() {
		return maximumactivetime;
	}

	public void setMaximumactivetime(String maximumactivetime) {
		this.maximumactivetime = maximumactivetime;
	}

	public String getPrototypecount() {
		return prototypecount;
	}

	public void setPrototypecount(String prototypecount) {
		this.prototypecount = prototypecount;
	}

	public Connection getConnection() throws SQLException {
		try {
			Properties dbProperties = new Properties();
			dbProperties.setProperty("proxool.maximum-connection-count",
					this.maximumconnectioncount);
			dbProperties.setProperty("proxool.minimum-connection-count",
					this.minimumconnectioncount);
			dbProperties.setProperty("proxool.house-keeping-test-sql",
					this.housekeepingtestsql);
			dbProperties.setProperty("user", this.uid);
			dbProperties.setProperty("password", pwd);
			String proxoolUrl = "proxool." + this.alias + ":"
					+ this.driverClass + ":" + this.url;
			return DriverManager.getConnection(proxoolUrl, dbProperties);
		} catch (Exception e) {
			throw new SQLException(e);
		}

	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return DriverManager.getConnection("proxool." + this.alias);
	}

	public PrintWriter getLogWriter() throws SQLException {
		
		return null;
	}

	public int getLoginTimeout() throws SQLException {
		
		return 0;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return null;
	}

	//@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
