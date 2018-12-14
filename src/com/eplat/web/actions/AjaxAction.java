package com.eplat.web.actions;

import com.eplat.core.IBizService;
import com.eplat.core.ValueObject;
import com.eplat.db.ConnectionManager;
import com.eplat.utils.FileUtils;
import com.eplat.utils.SpringManager;
import com.eplat.utils.StringUtils;
import com.eplat.web.ajax.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 针对Ajax前台请求的控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class AjaxAction extends BaseAction {
	/**
	 * 客户端调用，不返回结果集
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/call.do")
	public ModelAndView call(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AjaxResult result = new AjaxResult();
		ValueObject valueObject=null;
		try {
			valueObject = (ValueObject) this.getValueObject(request);
			valueObject.setResponse(response);

			loggingRequest(valueObject);
			
				if (StringUtils.hasLength(valueObject.getServiceName())) {
					IBizService service = null;
					service = (IBizService) SpringManager
							.getService(valueObject.getServiceName());
					String datasourceId = this.getDatasource(valueObject);

					if (StringUtils.hasLength(service.getDatasourceId())) {
						datasourceId = service.getDatasourceId();
					}
					// 设置服务类连接
					service.setDBConnection(ConnectionManager
							.getConnection(datasourceId));
					result = callServiceMethod(service, valueObject);
				} else {
					logger.error("服务类名称没有设置异常");
					result.setStatusCode(-1);
					result.setStatusMessage("服务类名称没有设置异常");
					result.setResult("服务类名称没有设置异常");
				}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setStatusCode(-1);
			result.setStatusMessage("执行服务类方法异常:" + e.getMessage());
			result.setResult(e.getStackTrace());
		} finally {
			ConnectionManager.closeConnections();
		}
		this.writeResponse(result, response,
				request.getParameter("jsoncallback"));
		return null;
	}

	/**
	 * 带事务的调用
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/calltrans.do")
	public ModelAndView callTrans(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AjaxResult result = new AjaxResult();
		ValueObject valueObject=null;
		try {
			valueObject = (ValueObject) this.getValueObject(request);
			valueObject.setResponse(response);

			loggingRequest(valueObject);
			
				if (StringUtils.hasLength(valueObject.getServiceName())) {
					IBizService service = null;
					service = (IBizService) SpringManager
							.getService(valueObject.getServiceName());
					String datasourceId = this.getDatasource(valueObject);

					if (StringUtils.hasLength(service.getDatasourceId())) {
						datasourceId = service.getDatasourceId();
					}
					// 设置服务类连接
					service.setDBConnection(ConnectionManager
							.getConnection(datasourceId));
					try {
						service.beginTrans();
						result = callServiceMethod(service, valueObject);
						if (result.getStatusCode() == -1) {
							service.rollback();
						} else {
							service.commit();
						}
					} catch (Exception e) {
						service.rollback();
						logger.error(e);
						result.setStatusCode(-1);
						result.setStatusMessage("执行服务类方法异常："+e.getMessage());
						result.setResult(e.getStackTrace());
					}
				} else {
					logger.error("服务类名称没有设置异常");
					result.setStatusCode(-1);
					result.setStatusMessage("服务类名称没有设置异常");
					result.setResult("服务类名称没有设置异常");
				}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setStatusCode(-1);
			result.setStatusMessage("执行服务类方法异常:" + e.getMessage());
			result.setResult(e.getStackTrace());
		} finally {
			ConnectionManager.closeConnections();
		}
		this.writeResponse(result, response,
				request.getParameter("jsoncallback"));
		return null;
	}

	/**
	 * 文件下载测试
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadTest.do")
	public ModelAndView downloadTest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ValueObject valueObject = (ValueObject) this.getValueObject(request);
		valueObject.setResponse(response);
		AjaxResult result = new AjaxResult();
		Iterator<Entry<String, String[]>> it = request.getParameterMap()
				.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String[]> data = it.next();
			System.out.println(data.getKey() + ":" + data.getValue()[0]);
		}
		result.setStatusCode(0);
		result.setStatusMessage("客户端调用成功");
		String filename = "c:\\2016office激活.zip";
		String displayName = FileUtils.getFileName(filename);
		displayName = new String(displayName.getBytes("GBK"), "iso-8859-1");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ displayName + "\"");
		ServletOutputStream out = response.getOutputStream();
		out.write(FileUtils.getFileStream(filename));
		out.flush();
		out.close();
		System.out.println("客户端调用");
		return null;
	}
	
	/**
	 * 文件下载
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/download.do")
	public ModelAndView download(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ValueObject valueObject = (ValueObject) this.getValueObject(request);
		valueObject.setResponse(response);
		AjaxResult result = new AjaxResult();
		Iterator<Entry<String, String[]>> it = request.getParameterMap()
				.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String[]> data = it.next();
			System.out.println(data.getKey() + ":" + data.getValue()[0]);
		}
		result.setStatusCode(0);
		result.setStatusMessage("客户端调用成功");
		
		String rootPath = request.getServletContext().getRealPath(String.valueOf(File.separatorChar));
		if (!rootPath.endsWith("\\")) {
			rootPath+="\\";
		}
		
		String relativepath=valueObject.getValue("relativepath");
		if (!"".equalsIgnoreCase(relativepath)) {
			
			String filename = rootPath+relativepath.replace("/", "\\");
			if (FileUtils.isExist(filename)) {
				String displayName = FileUtils.getFileName(filename);
				displayName = new String(displayName.getBytes("GBK"), "iso-8859-1");
				response.setContentType("multipart/form-data");
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ displayName + "\"");
				ServletOutputStream out = response.getOutputStream();
				out.write(FileUtils.getFileStream(filename));
				out.flush();
				out.close();
				//response.getWriter().println("下载完成");
			}else{
				//response.getWriter().println("文件不存在");
			}
		}else{
			//response.getWriter().println("文件不存在");
		}
		return null;
	}
}
