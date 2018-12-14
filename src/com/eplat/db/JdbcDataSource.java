package com.eplat.db;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * jdbc数据连接的管理类
 * @author Administrator
 *
 */
public class JdbcDataSource implements DataSource {
	private String url;
	private String uid;
	private String pwd;
	private String driverClass;
	private Properties connectionProperties = new Properties();
	private Driver driver;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	/**
	 * 
	 * getDriver
	 * 
	 * @描述：获取数据库驱动
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public Driver getDriver() throws DBException {
		try {
			Class clzz = Class.forName(driverClass);
			this.driver = DriverManager.getDriver(this.url);
			return this.driver;
		} catch (ClassNotFoundException e) {
			throw new DBException(e);
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	public Connection getConnection() throws SQLException {
		if (this.driver == null) {
			this.getDriver();
		}
		connectionProperties.setProperty("user", this.uid);
		connectionProperties.setProperty("password", this.pwd);
		return this.driver.connect(this.url, connectionProperties);
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		if (this.driver == null) {
			this.getDriver();
		}
		if (username != null) {
			connectionProperties.setProperty("user", username);
		}
		if (password != null) {
			connectionProperties.setProperty("password", password);
		}
		return this.driver.connect(this.url, connectionProperties);
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
