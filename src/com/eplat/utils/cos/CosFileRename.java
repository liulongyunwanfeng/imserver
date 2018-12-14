package com.eplat.utils.cos;

import com.eplat.utils.StringUtils;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * COS 文件更名类
 * 
 * @author 高洋
 * 
 */
public class CosFileRename implements FileRenamePolicy {

	public static final String GUID_FILENAME = "guid";// 更名为GUID文件名加原后缀
	public static final String SOURCE_GUID_FILENAME = "sourceguid";// 更名为原文件+'_'+guid+原后缀
	public static final String TIMESTAMP_FILENAME = "time";// 更名为原文件名+'_'+时间戳（精确到秒）+原后缀
	public static final String ORIGINAL_FILENAME = "original";// 原始的文件名称
	public static final String DEFAULT_FILENAME = "default";// 默认的文件更名规则，如果保存在文件已经存在将在文件后面加上0-9999的顺序号
	private String renameType;// 更名类型

	public CosFileRename() {

	}

	public CosFileRename(String type) {
		this.renameType = type;
	}

	public File rename(File f) {
		try {
			if (GUID_FILENAME.equalsIgnoreCase(this.renameType)) {
				return renameGuid(f);
			} else if (SOURCE_GUID_FILENAME.equalsIgnoreCase(this.renameType)) {
				return renameSourceGuid(f);
			} else if (TIMESTAMP_FILENAME.equalsIgnoreCase(this.renameType)) {
				return renameTime(f);
			} else if (ORIGINAL_FILENAME.equalsIgnoreCase(this.renameType)) {
				return renameOriginal(f);
			} else {
				return renameDefault(f);
			}
		} catch (Exception e) {
			return renameDefault(f);
		}

	}

	public File renameSourceGuid(File f) throws Exception {
		String name = f.getName();
		String body = null;
		String ext = null;
		int dot = name.lastIndexOf(".");
		if (dot != -1) {
			body = name.substring(0, dot);
			ext = name.substring(dot);
		} else {
			body = name;
			ext = "";
		}
		String newName = body + "_" + StringUtils.generateID() + ext;
		f = new File(f.getParent(), newName);
		return f;
	}

	public File renameGuid(File f) throws Exception {

		String name = f.getName();
		String body = null;
		String ext = null;
		int dot = name.lastIndexOf(".");
		if (dot != -1) {
			body = name.substring(0, dot);
			ext = name.substring(dot);
		} else {
			body = name;
			ext = "";
		}
		String newName = StringUtils.generateID() + ext;
		f = new File(f.getParent(), newName);
		return f;
	}

	/**
	 * 更名为时间戳
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public File renameTime(File f) throws Exception {
		String name = f.getName();
		String body = null;
		String ext = null;
		int dot = name.lastIndexOf(".");
		if (dot != -1) {
			body = name.substring(0, dot);
			ext = name.substring(dot);
		} else {
			body = name;
			ext = "";
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String newName = body + "_" + sdf.format(new Date()) + ext;
		f = new File(f.getParent(), newName);
		return f;
	}

	/**
	 * 原名
	 * 
	 * @param f
	 * @return
	 */
	public File renameOriginal(File f) {
		return f;
	}

	/**
	 * 默认命名
	 * 
	 * @param f
	 * @return
	 */
	public File renameDefault(File f) {
		if (createNewFile(f))
			return f;
		String name = f.getName();
		String body = null;
		String ext = null;
		int dot = name.lastIndexOf(".");
		if (dot != -1) {
			body = name.substring(0, dot);
			ext = name.substring(dot);
		} else {
			body = name;
			ext = "";
		}
		String newName;
		for (int count = 0; !createNewFile(f) && count < 9999; f = new File(f
				.getParent(), newName)) {
			count++;
			newName = body + "_" + count + ext;
		}
		return f;
	}

	private boolean createNewFile(File f) {
		try {
			return f.createNewFile();
		} catch (Exception ignored) {
			return false;
		}
	}

}
