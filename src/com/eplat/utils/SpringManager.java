package com.eplat.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.util.ArrayList;

/**
 * 管理SpringIOC的加载
 * @author Administrator
 *
 */
public class SpringManager {
	public static String[] locations = null;
	public static FileSystemXmlApplicationContext context = null;

	public static Object getService(String beanName) {
		if (context != null) {
			LoggerUtils.debug("IOC", "获取服务类[" + beanName + "]成功");			
			return context.getBean(beanName);
		} else {
			LoggerUtils.debug("IOC","Spring配置没有初始化加载，或者配置文件路径错误");		
			return null;
		}
	}

	public static void init() {
		try {
			ArrayList<String> fileList = new ArrayList<String>();
			String path = ResourceUtils.getWebClassesPath();
			path += "conf";
			if (FileUtils.isExist(path)) {
				File file = new File(path);
				File[] files = file.listFiles();
				if ((files != null) && (files.length > 0)) {
					for (int i = 0; i < files.length; i++) {
						String fileName = files[i].getName().toLowerCase();
						if (fileName.endsWith(".xml")
								&& fileName.startsWith("context-")) {
							LoggerUtils.debug("IOC","正在加载Spring配置文件：["
									+ files[i].getAbsolutePath() + "]");
							fileList.add(files[i].getAbsolutePath());
						}
					}
				}
				if (fileList.size() > 0) {
					locations = new String[fileList.size()];
					for (int i = 0; i < fileList.size(); i++) {
						locations[i] = "file:" + fileList.get(i);
					}
				}
			}
			if (locations != null) {
				context = new FileSystemXmlApplicationContext(locations);
				LoggerUtils.debug("IOC","IOC容器初始化成功");
			}
		} catch (BeansException e) {
			LoggerUtils.error("IOC","IOC 初始化失败，失败原因："+e.getMessage());
		}
	}

}
