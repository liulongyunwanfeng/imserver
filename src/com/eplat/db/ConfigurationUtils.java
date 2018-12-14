package com.eplat.db;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * 数据库连接的工具类，负责从文件加载配置到内存
 * 
 * @author Administrator
 * 
 */

public class ConfigurationUtils {
	/**
	 * 
	 * getInputStream
	 * 
	 * @描述：根据文件名称来获取文件的输入流
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static InputStream getInputStream(String fileName)
			throws DBException {
		File file = new File(fileName);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new DBException(90006, e);
		}
	}

	/**
	 * 
	 * loadConfig
	 * 
	 * @描述：从输入流中加载数据库连接配置
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public static Configuration loadConfig(java.io.InputStream in)
			throws DBException {
		try {
			Configuration config = new Configuration();
			SAXBuilder saxBuilder = new SAXBuilder(false);
			saxBuilder.setExpandEntities(false);
			Document doc = saxBuilder.build(in);
			Element parentEl = doc.getRootElement();
			if (parentEl != null) {
				config.setAssistInfo("dbid", parentEl.getAttributeValue("id"));
				config.setAssistInfo("dbname",
						parentEl.getAttributeValue("name"));
				List parentList = parentEl.getChildren();
				for (int i = 0; i < parentList.size(); i++) {
					Element childEl = null;
					childEl = (Element) parentList.get(i);
					config.setAssistInfo(childEl.getName().toLowerCase(),
							childEl.getTextTrim());

				}
			}
			config.initDataSource();
			return config;
		} catch (JDOMException e) {
			throw new DBException(e);
		} catch (Exception e) {
			throw new DBException(e);
		}
	}
}
