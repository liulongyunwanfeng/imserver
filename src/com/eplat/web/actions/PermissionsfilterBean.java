package com.eplat.web.actions;

import com.eplat.utils.FileUtils;
import com.eplat.utils.ResourceUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 无需进行权限认证的java服务Bean
 * 
 * @author Administrator
 * 
 */
public class PermissionsfilterBean {
	// 服务类名称
	private String servicename;
	// 方法名称
	private String methodname;
	// 过滤描述
	private String filterdesc;

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public String getFilterdesc() {
		return filterdesc;
	}

	public void setFilterdesc(String filterdesc) {
		this.filterdesc = filterdesc;
	}

	public static void parseFilter() {
		String path = ResourceUtils.getWebClassesPath();
		path += "conf";
		path = path + "/permissions-filter.xml";
		if (FileUtils.isExist(path)) {
			try {				
				SAXBuilder saxBuilder = new SAXBuilder(false);
				saxBuilder.setExpandEntities(false);
				Document doc =saxBuilder.build(new File(path));
				Element parentEl = doc.getRootElement();
				if (parentEl != null) {
					List parentList = parentEl.getChildren();
					for (int i = 0; i < parentList.size(); i++) {
						Element childEl = (Element) parentList.get(i);
						PermissionsfilterBean filter=new PermissionsfilterBean();
						filter.setServicename(childEl.getAttributeValue("service"));
						filter.setMethodname(childEl.getAttributeValue("method"));
						filter.setFilterdesc(childEl.getTextTrim());
						BaseAction.addFilter(filter);
					}
				}
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
