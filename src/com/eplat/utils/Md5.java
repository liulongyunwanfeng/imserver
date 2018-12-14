package com.eplat.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.InputStream;
/**
 * 加密解密封装类
 * 
 * @author Administrator
 *
 */
public class Md5 {
	/**
	 * 获取字符串的md5
	 * @param inbuf
	 * @return
	 */
	public String md5(String inbuf) {
		return DigestUtils.md5Hex(inbuf);
	}
	/**
	 * 获取字节流的md5
	 * @param data
	 * @return
	 */
	public String md5(byte[] data){
		return DigestUtils.md5Hex(data);
	}
	/**
	 * 获取输入流的Md5
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public String md5(InputStream in) throws Exception{
		return DigestUtils.md5Hex(in);
	}
}
