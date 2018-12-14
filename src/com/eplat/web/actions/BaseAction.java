package com.eplat.web.actions;

import com.eplat.core.IBizService;
import com.eplat.core.ValueObject;
import com.eplat.db.ConnectionManager;
import com.eplat.db.DBConnection;
import com.eplat.db.DBException;
import com.eplat.utils.StringUtils;
import com.eplat.web.ajax.AjaxResult;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * 
 * @项目名称：eplatweb
 * @类名称：BaseAction
 * @类描述：所有请求的父类
 * @创建人：高洋
 * @创建时间：2011-5-7 下午02:12:57
 * @修改人：高洋
 * @修改时间：2011-5-7 下午02:12:57
 * @修改备注：
 * @version 1.0.0.1
 * 
 */
public class BaseAction {
	public static Logger logger = Logger.getLogger("actions");
	public static List<PermissionsfilterBean> filterList = new ArrayList<PermissionsfilterBean>();
	/**
	 * 获取ValueObject
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ValueObject getValueObject(HttpServletRequest request) throws Exception{
		boolean jsonFlag=false;
		ValueObject vo=null;
		if (StringUtils.hasLength(request.getParameter("remote_args"))){
			String input = URLDecoder.decode(request.getParameter("remote_args"),
					"UTF-8");
			JSONObject jsonObject = JSONObject.fromObject(input);
			vo = (ValueObject) JSONObject.toBean(jsonObject,
					ValueObject.class);
			vo.setRequest(request);
			jsonFlag=true;
		} else {
			vo=new ValueObject();
			vo.setRequest(request);
			jsonFlag=false;
		}
		
		Iterator<Entry<String,String[]>> it=request.getParameterMap().entrySet().iterator();
		while(it.hasNext()){
			Entry<String,String[]> data=it.next();
			if ("remote_args".equalsIgnoreCase(data.getKey())){
				continue;
			}
			if (data.getValue()!=null){				
				if (data.getValue().length==1){
					vo.setFormValue(data.getKey(), data.getValue()[0]);
					if (!jsonFlag){
						if ("_serviceName".equalsIgnoreCase(data.getValue()[0])){
							vo.setServiceName(data.getValue()[0]);
						} else if ("_methodName".equalsIgnoreCase(data.getValue()[0])){
							vo.setMethodName(data.getValue()[0]);
						} else if ("_currentPage".equalsIgnoreCase(data.getValue()[0])){
							if (StringUtils.canNumber(data.getValue()[0])){
								vo.setCurrentPage(new BigDecimal(data.getValue()[0]).intValue());
							}							
						} else if ("_pageSize".equalsIgnoreCase(data.getValue()[0])){
							if (StringUtils.canNumber(data.getValue()[0])){
								vo.setPageSize(new BigDecimal(data.getValue()[0]).intValue());
							}							
						} else if ("_gotoPage".equalsIgnoreCase(data.getValue()[0])){
							if (StringUtils.canNumber(data.getValue()[0])){
								vo.setGotoPage(new BigDecimal(data.getValue()[0]).intValue());
							}							
						} else if ("_queryId".equalsIgnoreCase(data.getValue()[0])){
							vo.setQueryId(data.getValue()[0]);
						} else if ("_sortExpr".equalsIgnoreCase(data.getValue()[0])){
							vo.setSortExpr(data.getValue()[0]);
						} else if ("_tokenid".equalsIgnoreCase(data.getValue()[0])){
							vo.setTokenid(data.getValue()[0]);
						}
					}
				} 
				else {
					vo.setFormValue(data.getKey(), data.getValue());
				}	
				
			}			
		}
		return vo;
	}
	/**
	 * 验证是否需要进行session过滤，返回true，需要session过滤，否则返回false
	 * 
	 * @param serviceName
	 * @param methodName
	 * @return
	 */
	public static boolean checkFilter(String serviceName, String methodName) {
		boolean rtn = false;
		if (filterList != null && filterList.size() > 0 && serviceName != null
				&& methodName != null) {
			for (int i = 0; i < filterList.size(); i++) {
				boolean methodflag = false;
				boolean serivceflag = false;
				String service = filterList.get(i).getServicename();
				String method = filterList.get(i).getMethodname();
				if ("*".equalsIgnoreCase(service)) {
					serivceflag = true;
				} else if (service.startsWith("*")) {
					service = service.substring(1);
					serivceflag = serviceName.endsWith(service);
				} else if (service.endsWith("*")) {
					service = service.substring(0, service.length() - 1);
					serivceflag = serviceName.startsWith(service);
				} else {
					serivceflag = serviceName.equalsIgnoreCase(service);
				}
				if ("*".equalsIgnoreCase(method)) {
					methodflag = true;
				} else if (method.startsWith("*")) {
					method = method.substring(1);
					methodflag = methodName.endsWith(method);
				} else if (method.endsWith("*")) {
					method = method.substring(0, method.length() - 1);
					methodflag = methodName.startsWith(method);
				} else {
					methodflag = methodName.equalsIgnoreCase(method);
				}
				rtn = serivceflag && methodflag;
				if (rtn) {
					break;
				}
			}
		}
		return rtn;
	}

	/**
	 * 添加过滤器方法
	 * 
	 * @param filter
	 */
	public static void addFilter(PermissionsfilterBean filter) {
		if (filterList == null) {
			filterList = new ArrayList<PermissionsfilterBean>();
		}
		filterList.add(filter);
	}


	/**
	 * 
	 * writeResponse
	 * 
	 * @描述：将返回结果写入影响对象
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public void writeResponse(AjaxResult result, HttpServletResponse response,String jsoncallback)
			throws Exception {
		try {
			response.setContentType("application/json; charset=utf-8");
			response.setHeader("Cache-Control",
					"no-store, no-cache, must-revalidate, max-age=0");
			PrintWriter writer = response.getWriter();
			if (StringUtils.hasLength(jsoncallback)){ //说明是跨域问题
				String jsonStr=JSONObject.fromObject(result).toString();
				writer.write(jsoncallback+"("+jsonStr+")");
			} else{
				writer.write(JSONObject.fromObject(result).toString());
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @方法名称:getConnection
	 * @方法说明：获取數数据连接
	 * @返回类型：DBConnection
	 * @参数说明
	 * @param valueObject
	 * @return
	 * @throws DBException
	 */
	public DBConnection getConnection(ValueObject valueObject) throws Exception {
		String datasourceId = "";
		HttpSession session = valueObject.getRequest().getSession();
		datasourceId = (String) session.getAttribute("datasourceId");
		if (StringUtils.hasLength(datasourceId)) {
			return ConnectionManager.getConnection(datasourceId);
		} else if (StringUtils.hasLength(valueObject.getDsId())) {
			return ConnectionManager.getConnection(valueObject.getDsId());
		} else {
			return ConnectionManager.getConnection();
		}
	}

	public String getDatasource(ValueObject valueObject) {
		String datasourceId = "";
		HttpSession session = valueObject.getRequest().getSession();
		datasourceId = (String) session.getAttribute("datasourceId");
		if (StringUtils.hasLength(datasourceId)) {
			return datasourceId;
		} else {
			return "default";
		}

	}


	
	/**
	 * 
	 * @方法名称:callServiceMethod
	 * @方法说明：调用服务类方法
	 * @返回类型：AjaxResult
	 * @参数说明
	 * @param valueObject
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AjaxResult callServiceMethod(IBizService service,
			ValueObject valueObject) throws Exception {

		AjaxResult result;
		// 获取方法名称
		Method method = this.getMethod(service, valueObject.getMethodName());

		Object[] objs = new Object[1];
		objs[0] = valueObject;
		result = (AjaxResult) method.invoke(service, objs);
		result.setClientId(valueObject.getClientId());
		result.setShowErrors(valueObject.getShowErrors());
		return result;
	}

	/**
	 * 根据方法名称来获取方法
	 * 
	 * @param obj
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Method getMethod(Object obj, String name) throws Exception {
		Method method = null;
		try {
			Class[] parameterTypes = new Class[1];
			parameterTypes[0] = ValueObject.class;
			method = obj.getClass().getMethod(name, parameterTypes);
		} catch (SecurityException e) {
			throw new Exception(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new Exception(e.getMessage());
		}
		return method;
	}

	/**
	 * 
	 * @方法名称:loggingRequest
	 * @方法说明：记录日志，输出到actions.log日志文件
	 * @返回类型：void
	 * @参数说明
	 * @param valueObject
	 */
	public void loggingRequest(ValueObject valueObject) {
		String message = "前台发起：";
		if (StringUtils.hasLength(valueObject.getServiceName())) {
			message = message + "，服务名称为：" + valueObject.getServiceName();
		}
		if (StringUtils.hasLength(valueObject.getMethodName())) {
			message = message + "，方法名称为：" + valueObject.getMethodName();
		}
		logger.info(message + "调用");
	}

}
