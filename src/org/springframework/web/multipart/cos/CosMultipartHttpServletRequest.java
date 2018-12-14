package org.springframework.web.multipart.cos;

import com.oreilly.servlet.MultipartRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

/**
 * MultipartHttpServletRequest implementation for Jason Hunter's COS. Wraps a
 * COS MultipartRequest with Spring MultipartFile instances.
 * 
 * <p>
 * Not intended for direct application usage. Application code can cast to this
 * implementation to access the underlying COS MultipartRequest, if it ever
 * needs to.
 * 
 * @author Juergen Hoeller
 * @since 06.10.2003
 * @see CosMultipartResolver
 * @see com.oreilly.servlet.MultipartRequest
 */
public class CosMultipartHttpServletRequest extends
		AbstractMultipartHttpServletRequest {

	protected static final Log logger = LogFactory
			.getLog(CosMultipartHttpServletRequest.class);

	private final MultipartRequest multipartRequest;

	/**
	 * Wrap the given HttpServletRequest in a MultipartHttpServletRequest.
	 * 
	 * @param request
	 *            the servlet request to wrap
	 * @param multipartRequest
	 *            the COS multipart request to wrap
	 */
	protected CosMultipartHttpServletRequest(HttpServletRequest request,
			MultipartRequest multipartRequest) {
		super(request);
		this.multipartRequest = multipartRequest;
		setMultipartFiles(initFileMap(multipartRequest));
	}

	/**
	 * Return the underlying <code>com.oreilly.servlet.MultipartRequest</code>
	 * instance. There is hardly any need to access this.
	 */
	public MultipartRequest getMultipartRequest() {
		return multipartRequest;
	}

	/**
	 * Initialize a Map with file name Strings as keys and CosMultipartFile
	 * instances as values.
	 * 
	 * @param multipartRequest
	 *            the COS multipart request to get the multipart file data from
	 * @return the Map with CosMultipartFile values
	 */
	private MultiValueMap<String, MultipartFile> initFileMap(
			MultipartRequest multipartRequest) {
		MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap<String, MultipartFile>();
		Enumeration<String> fileNames = multipartRequest.getFileNames();
		if (fileNames != null) {
			while (fileNames.hasMoreElements()) {
				String fileName = fileNames.nextElement();
				files.add(fileName, new CosMultipartFile(fileName));
			}
		}
		return files;
	}

	public Enumeration getParameterNames() {
		return this.multipartRequest.getParameterNames();
	}

	public String getParameter(String name) {
		return this.multipartRequest.getParameter(name);
	}

	public String[] getParameterValues(String name) {
		return this.multipartRequest.getParameterValues(name);
	}

	public Map getParameterMap() {
		Map params = new HashMap();
		Enumeration names = getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			params.put(name, getParameterValues(name));
		}
		return Collections.unmodifiableMap(params);
	}

	/**
	 * Implementation of Spring's MultipartFile interface on top of a COS
	 * MultipartRequest object.
	 */
	private class CosMultipartFile implements MultipartFile {

		private final String name;

		private final File file;

		private final long size;

		public CosMultipartFile(String name) {
			this.name = name;
			this.file = multipartRequest.getFile(this.name);
			this.size = (file != null ? file.length() : 0);
		}

		public String getName() {
			return name;
		}

		public boolean isEmpty() {
			return (this.size == 0);
		}

		public String getOriginalFilename() {
			String filename = multipartRequest.getOriginalFileName(this.name);
			return (filename != null ? filename : "");
		}

		public String getContentType() {
			return multipartRequest.getContentType(this.name);
		}

		public long getSize() {
			return size;
		}

		public byte[] getBytes() throws IOException {
			if (this.file != null && !this.file.exists()) {
				throw new IllegalStateException(
						"File has been moved - cannot be read again");
			}
			return (this.size != 0 ? FileCopyUtils.copyToByteArray(this.file)
					: new byte[0]);
		}

		public InputStream getInputStream() throws IOException {
			if (this.file != null && !this.file.exists()) {
				throw new IllegalStateException(
						"File has been moved - cannot be read again");
			}
			if (this.size != 0) {
				return new FileInputStream(this.file);
			} else {
				return new ByteArrayInputStream(new byte[0]);
			}
		}

		public void transferTo(File dest) throws IOException,
				IllegalStateException {
			if (this.file != null && !this.file.exists()) {
				throw new IllegalStateException(
						"File has already been moved - cannot be transferred again");
			}

			if (dest.exists() && !dest.delete()) {
				throw new IOException("Destination file ["
						+ dest.getAbsolutePath()
						+ "] already exists and could not be deleted");
			}

			boolean moved = false;
			if (this.file != null) {
				moved = this.file.renameTo(dest);
				if (!moved) {
					FileCopyUtils.copy(this.file, dest);
				}
			} else {
				dest.createNewFile();
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Multipart file '"
						+ getName()
						+ "' with original filename ["
						+ getOriginalFilename()
						+ "], stored "
						+ (this.file != null ? "at ["
								+ this.file.getAbsolutePath() + "]"
								: "in memory") + ": "
						+ (moved ? "moved" : "copied") + " to ["
						+ dest.getAbsolutePath() + "]");
			}
		}
	}

	@Override
	public HttpHeaders getMultipartHeaders(String paramOrFileName) {
		try {
			Part part = getPart(paramOrFileName);
			if (part != null) {
				HttpHeaders headers = new HttpHeaders();
				for (String headerName : part.getHeaderNames()) {
					headers.put(headerName,
							new ArrayList<String>(part.getHeaders(headerName)));
				}
				return headers;
			} else {
				return null;
			}
		} catch (Exception ex) {
			throw new MultipartException(
					"Could not access multipart servlet request", ex);
		}
	}

	@Override
	public String getMultipartContentType(String arg0) {
		return multipartRequest.getContentType(arg0);
	}

}
