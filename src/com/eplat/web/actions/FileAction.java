package com.eplat.web.actions;

import com.eplat.utils.FileUtils;
import com.eplat.utils.StringUtils;
import com.eplat.web.ajax.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件处理Action
 * 
 * @author Administrator
 *
 */
@Controller
public class FileAction extends BaseAction {
	/**
	 * 存储位置
	 */
	private Map<String, StoreBean> storeMaps = null;

	public Map<String, StoreBean> getStoreMaps() {
		return storeMaps;
	}

	public void setStoreMaps(Map<String, StoreBean> storeMaps) {
		this.storeMaps = storeMaps;
	}

	/**
	 * 获取文件名称
	 * 
	 * @param originalFile
	 * @param storeBean
	 * @return
	 * @throws Exception
	 */
	private String getFileName(String originalFile, StoreBean storeBean)
			throws Exception {
		String fileExt = StringUtils.getFileNameExtension(originalFile);
		if ("src_guid".equalsIgnoreCase(storeBean.getRename())) {// 原文件名称+"_guid"
			if (StringUtils.hasLength(fileExt)) {
				return StringUtils.stripFileNameExtension(originalFile) + "_"
						+ StringUtils.generateID() + "." + fileExt;
			} else {
				return StringUtils.stripFileNameExtension(originalFile) + "_"
						+ StringUtils.generateID();
			}
		} else if ("guid".equalsIgnoreCase(storeBean.getRename())) {
			if (StringUtils.hasLength(fileExt)) {
				return StringUtils.generateID() + "." + fileExt;
			} else {
				return StringUtils.generateID();
			}
		} else if ("timestamp".equalsIgnoreCase(storeBean.getRename())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			if (StringUtils.hasLength(fileExt)) {
				return sdf.format(new Date()) + "." + fileExt;
			} else {
				return sdf.format(new Date());
			}
		} else if ("src_timestamp".equalsIgnoreCase(storeBean.getRename())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			if (StringUtils.hasLength(fileExt)) {
				return StringUtils.stripFileNameExtension(originalFile) + "_"
						+ sdf.format(new Date()) + "." + fileExt;
			} else {
				return StringUtils.stripFileNameExtension(originalFile) + "_"
						+ sdf.format(new Date());
			}
		} else {
			return originalFile;
		}
	}

	/**
	 * 单文件上传
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/singleFile")
	public String singleFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			StoreBean storeBean = null;
			String storeId = request.getParameter("storeId");
			if (StringUtils.hasLength(storeId)) {
				storeBean = storeMaps.get(storeId);
			} else {
				storeBean = storeMaps.get("default");
			}
			if (storeBean == null) {
				result.setStatusCode(-1);
				result.setStatusMessage("服务器参数配置错误，无法获取服务器的配置信息！");
				this.writeResponse(result, response, null);
				return null;
			}
			// 绝对路径
			String realPath = "";
			// 相对路径
			String relativePath = "";
			if (storeBean.getPath().startsWith("absolute:")) {// 说明绝对路径
				realPath = storeBean.getPath().substring(9);
				relativePath = "";
			} else {
				ServletContext sc = request.getSession().getServletContext();
				realPath = sc.getRealPath(storeBean.getPath());
				relativePath = storeBean.getPath();
				if (!relativePath.endsWith("/")) {
					relativePath = relativePath + "/";
				}
			}
			if (!realPath.endsWith("/")) {
				realPath = realPath + "/";
			}
			
			File dirPath = new File(realPath); 
			if (!dirPath.exists()) {  
				dirPath.mkdirs();  
	        }  
			
			CommonsMultipartResolver cmr = new CommonsMultipartResolver(
					request.getServletContext());
			if (cmr.isMultipart(request)) {
				// 开始取文件 进行获取
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) (request);
				Iterator<String> files = mRequest.getFileNames();
				if (files.hasNext()) {
					MultipartFile mFile = mRequest.getFile(files.next());
					if (mFile != null) {
						String originalname = mFile.getOriginalFilename();
						String fileName = this.getFileName(originalname,
								storeBean);
						String realFileName = realPath + fileName;
						String relativeFileName = relativePath + fileName;
						File localFile = new File(realFileName);
						mFile.transferTo(localFile);
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("originalname", originalname);
						data.put("filename", fileName);
						data.put("relativeFile", relativeFileName);
						data.put("realFile", realFileName);
						// 文件大小
						data.put("fileSize",
								FileUtils.getFileSize(realFileName));
						if (storeBean.getImageFlag() == 1) {
							try {
								FileInputStream fis = new FileInputStream(localFile);
								BufferedImage sourceImg = ImageIO.read(fis);
								data.put("width",
										String.valueOf(sourceImg.getWidth()));
								data.put("height",
										String.valueOf(sourceImg.getHeight()));
								fis.close();
								fis = null;
								sourceImg = null;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						result.setResult(data);
						result.setStatusCode(0);
						result.setStatusMessage("文件上传成功！");
					}
				}
			}
		} catch (Exception e) {
			result.setStatusCode(-1);
			result.setStatusMessage("文件处理异常：" + e.getMessage());
		}
		this.writeResponse(result, response, null);
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param files
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/deleteFile")
	public String deleteFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			if ("absolute".equalsIgnoreCase(request.getParameter("flag"))){
				FileUtils.delete(request.getParameter("filename"));
				result.setStatusCode(0);
				result.setStatusMessage("删除文件失败");
			} else{
				StoreBean storeBean = null;
				String storeId = request.getParameter("storeId");
				if (StringUtils.hasLength(storeId)) {
					storeBean = storeMaps.get(storeId);
				} else {
					storeBean = storeMaps.get("default");
				}
				if (storeBean == null) {
					result.setStatusCode(-1);
					result.setStatusMessage("服务器参数配置错误，无法获取服务器的配置信息！");
					this.writeResponse(result, response, null);
					return null;
				}
				// 绝对路径
				String realPath = "";
				// 相对路径
				String relativePath = "";
				if (storeBean.getPath().startsWith("absolute:")) {// 说明绝对路径
					realPath = storeBean.getPath().substring(9);
					relativePath = "";
				} else {
					ServletContext sc = request.getSession().getServletContext();
					realPath = sc.getRealPath(storeBean.getPath());
					relativePath = storeBean.getPath();
					if (!relativePath.endsWith("/")) {
						relativePath = relativePath + "/";
					}
				}
				if (!realPath.endsWith("/")) {
					realPath = realPath + "/";
				}
				String realFileName=realPath+request.getParameter("filename");
				FileUtils.delete(request.getParameter("filename"));
				result.setStatusCode(0);
				result.setStatusMessage("删除文件失败");
			}
		} catch (Exception e) {
			result.setStatusCode(-1);
			result.setStatusMessage("文件处理异常：" + e.getMessage());
		}
		this.writeResponse(result, response, null);
		return null;
	}

	/**
	 * 多文件上传
	 * 
	 * @param files
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/muliFile")
	public String muliFile(@RequestParam("file") CommonsMultipartFile files[],
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			StoreBean storeBean = null;
			String storeId = request.getParameter("storeId");
			if (StringUtils.hasLength(storeId)) {
				storeBean = storeMaps.get(storeId);
			} else {
				storeBean = storeMaps.get("default");
			}
			if (storeBean == null) {
				result.setStatusCode(-1);
				result.setStatusMessage("服务器参数配置错误，无法获取服务器的配置信息！");
				this.writeResponse(result, response, null);
				return null;
			}
			// 绝对路径
			String realPath = "";
			// 相对路径
			String relativePath = "";
			if (storeBean.getPath().startsWith("absolute:")) {// 说明绝对路径
				realPath = storeBean.getPath().substring(9);
				relativePath = "";
			} else {
				ServletContext sc = request.getSession().getServletContext();
				realPath = sc.getRealPath(storeBean.getPath());
				relativePath = storeBean.getPath();
				if (!relativePath.endsWith("/")) {
					relativePath = relativePath + "/";
				}
			}
			if (!realPath.endsWith("/")) {
				realPath = realPath + "/";
			}
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < files.length; i++) {
				// 开始取文件 进行获取
				String originalname = files[i].getOriginalFilename();
				String fileName = this.getFileName(originalname, storeBean);
				String realFileName = realPath + fileName;
				String relativeFileName = relativePath + fileName;
				File localFile = new File(realFileName);
				files[i].transferTo(localFile);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("originalname", originalname);
				data.put("filename", fileName);
				data.put("relativeFile", relativeFileName);
				data.put("realFile", realFileName);
				// 文件大小
				data.put("fileSize", FileUtils.getFileSize(realFileName));
				try {
					if (storeBean.getImageFlag() == 1) {
						FileInputStream fis = new FileInputStream(localFile);
						BufferedImage sourceImg = ImageIO.read(fis);
						data.put("width", String.valueOf(sourceImg.getWidth()));
						data.put("height", String.valueOf(sourceImg.getHeight()));
						fis.close();
						fis = null;
						sourceImg = null;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				datas.add(data);
			}
			result.setResult(datas);
			result.setStatusCode(0);
			result.setStatusMessage("文件上传成功！");
		} catch (Exception e) {
			result.setStatusCode(-1);
			result.setStatusMessage("文件处理异常：" + e.getMessage());
		}
		this.writeResponse(result, response, null);
		return null;

	}
}
