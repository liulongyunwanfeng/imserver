package com.eplat.utils;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.UUID;

public class AES {
	public static void main(String[] args) throws Exception {
		/*
		 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
		 */
		// String cKey = "1234567890abcDEF";
		String Key = "zhumingshengyygs";
		// 需要加密的字串
		String account = "aaaf62b40b7b3b7955e99b2e225e6970f2a1b865bd53958d5ee2aeb1c03a3cd85f40bd4ecda5110967e9713c0ca5b6bcd38a4b5ec40d91953c11678e24efb773311aa92e11cdf81bd8091f8a9f695ab036c442ba4b7a4920b61e7fc0c2bad8ae50e5c43474b61ca9cc54c6c36c2cc7c62eb21f6990252ef398e0d950f32d0a11";// 筑民生用户票据
		String uuid = UUID.randomUUID().toString();
		long currentTimeMillis = System.currentTimeMillis();
		String timestamp = String.valueOf(currentTimeMillis);// 时间戳
		String src = uuid + "&" + account + "&" + timestamp;
		String base64 = Base64.getBase64(src);
		System.out.println("时间戳：" + currentTimeMillis);
		System.out.println("时间戳：" + timestamp);
		System.out.println("拼接后：" + src);
		System.out.println("base64：" + base64);

		// 加密
		long lStart = System.currentTimeMillis();
		String enString = AES.Encrypt(base64, Key);
		System.out.println("加密后的字串是：" + enString);
		long lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("加密耗时：" + lUseTime + "毫秒");
		// 解密
		enString="aaaf62b40b7b3b7955e99b2e225e6970f2a1b865bd53958d5ee2aeb1c03a3cd85f40bd4ecda5110967e9713c0ca5b6bcd38a4b5ec40d91953c11678e24efb773311aa92e11cdf81bd8091f8a9f695ab036c442ba4b7a4920b61e7fc0c2bad8ae50e5c43474b61ca9cc54c6c36c2cc7c62eb21f6990252ef398e0d950f32d0a11";
		lStart = System.currentTimeMillis();
		String DeString = AES.Decrypt(enString, Key);
		System.out.println("解密后的字串是：" + DeString);
		System.out.println("解密后base64的字串是：" + Base64.getFromBase64(DeString));
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("解密耗时：" + lUseTime + "毫秒");
	}

	public static String Decrypt(String sSrc, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2byte(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	// 判断Key是否正确
	public static String Encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			System.out.print("Key为空null");
			return null;
		}
		
		byte[] raw = sKey.getBytes("ASCII");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		return byte2hex(encrypted).toLowerCase();
	}

	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
					16);
		}
		return b;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	public static String encrypt128(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);

			// Base64 encode = new Base64();

			BASE64Encoder encode = new BASE64Encoder();
			return encode.encode(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String EncryptAES(String data, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE,
				new SecretKeySpec(key.getBytes("UTF-8"), "AES"));
		byte[] result = cipher.doFinal(data.getBytes("UTF-8"));

		return new BASE64Encoder().encode(result);
	}
}
