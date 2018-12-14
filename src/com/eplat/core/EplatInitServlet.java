package com.eplat.core;

import com.eplat.db.ConnectionManager;
import com.eplat.utils.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class EplatInitServlet extends HttpServlet {
	private static final long serialVersionUID = -4868990834722863781L;

	public void destroy() {

		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				LoggerUtils.debug("DB",
						String.format("卸载 jdbc driver: %s", driver));
			} catch (SQLException e) {
				LoggerUtils.debug("DB",
						String.format("卸载 jdbc driver 失败 %s", driver));
			}
		}
		super.destroy();

	}

	public void init() throws ServletException {
		System.out.println("初始化[系统日志]");
		try {
			/**
			 * 创建用于交换的临时路径
			 */
			String temporaryPath = this.getInitParameter("TEMPORARY_PATH");
			if (!StringUtils.hasLength(temporaryPath)) {
				temporaryPath = ResourceUtils.getWebRoot() + "temporary";
			} else if (StringUtils.startsWithIgnoreCase(temporaryPath, "file:")) {
				temporaryPath = temporaryPath.substring(6);
			}
			if (!FileUtils.isExist(temporaryPath)) {
				FileUtils.createDir(temporaryPath);
			}
			EplatConfig.TEMPORARY_PATH = temporaryPath;
			// 售票初始化参数

			// 初始化spring容器
			SpringManager.init();
		
			RedisUtils.init();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionManager.closeConnections();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static long getTimeMillis(String time) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " "
					+ time);
			return curDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
