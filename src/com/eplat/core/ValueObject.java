package com.eplat.core;

import com.eplat.utils.FileUtils;
import com.eplat.utils.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @项目名称：eplat
 * @类名称：ValueObject
 * @类描述：值对象
 * @创建人：高洋
 * @创建时间：2010-3-1 下午11:59:52
 * @修改人：高洋
 * @修改时间：2010-3-1 下午11:59:52
 * @修改备注：
 * @version
 */
public class ValueObject implements java.io.Serializable {
	// 参数列表
	private Map<String, Object> valueMap = new HashMap<String, Object>();
	// 服务名称
	private String serviceName;
	// 方法名称
	private String methodName;
	// 当前页码
	private int currentPage;
	// 每页显示记录行数
	private int pageSize = 20;
	// 跳转页面
	private int gotoPage;
	// 总页数
	private int totalPage;
	private HttpServletRequest request;
	private HttpServletResponse response;
	// 令牌id
	private String tokenid;

	
	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	// 默认DBID
	private String dsId = "default";
	// 排序表达式
	private String sortExpr = "";
	// 查询ID
	private String queryId = "";
	// 客户端请求ID
	private String clientId = "";
	private String showErrors = "false";
	// 导出类型
	private String exportType = "";

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getShowErrors() {
		return showErrors;
	}

	public void setShowErrors(String showErrors) {
		this.showErrors = showErrors;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getSortExpr() {
		return sortExpr;
	}

	public void setSortExpr(String sortExpr) {
		this.sortExpr = sortExpr;
	}

	public String getDsId() {
		return dsId;
	}

	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

	public Map<String, Object> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getGotoPage() {
		return gotoPage;
	}

	public void setGotoPage(int gotoPage) {
		this.gotoPage = gotoPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setFormValue(String key, Object value) {
		valueMap.put(key, value);
	}

	public Object getFormValue(String key) {
		return valueMap.get(key);
	}

	public String getValue(String key) {
		if (valueMap.get(key) == null) {
			return "";
		} else {
			return String.valueOf(valueMap.get(key));
		}
	}

	public Integer getInt(String key) {
		if (valueMap.get(key) == null) {
			return null;
		} else {
			return Integer.parseInt(String.valueOf(valueMap.get(key)));
		}
	}

	public BigDecimal getBigDecimal(String key) {
		if (valueMap.get(key) == null) {
			return null;
		} else {
			return new BigDecimal(String.valueOf(valueMap.get(key)));
		}
	}

	/**
	 * 写输出流到response对象，用于文件下载
	 * 
	 * @param realFileName
	 * @param displayName
	 */
	public void writeFile(String realFileName, String displayName)
			throws Exception {
		String display = "";
		if (!StringUtils.hasLength(displayName)) {
			display = FileUtils.getFileName(realFileName);
		} else {
			display = displayName;
		}
		display = new String(display.getBytes("GBK"), "iso-8859-1");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ display + "\"");
		ServletOutputStream out = response.getOutputStream();
		out.write(FileUtils.getFileStream(realFileName));
		out.flush();
		out.close();
	}

	private static final long serialVersionUID = 1L;

}
