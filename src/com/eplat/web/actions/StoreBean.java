package com.eplat.web.actions;

/**
 * 上传文件存储方式
 * 
 * @author Administrator
 *
 */
public class StoreBean {
	// 存储Id
	private String storeId;
	/**
	 * 存储路径 absolute:开头的表示绝对路径 否则为对象路径
	 */
	private String path;
	// 文件大小
	private long maxFileSize;
	/**
	 * 文件命名规则 guid 以uuid命名 src_guid 原文件名+"_guid"命名 timestamp 时间戳 src_timestamp
	 * 原文件名+"_timestamp"命名 为空或者为null 即为src原文件
	 */
	private String rename;
	//文件文件标识，如果是图片文件系统会读取文件图片的大小，用于Wap的缩放
	private int imageFlag;
	
	public int getImageFlag() {
		return imageFlag;
	}

	public void setImageFlag(int imageFlag) {
		this.imageFlag = imageFlag;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public String getRename() {
		return rename;
	}

	public void setRename(String rename) {
		this.rename = rename;
	}

}
