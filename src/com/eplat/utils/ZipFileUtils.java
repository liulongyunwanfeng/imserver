package com.eplat.utils;

import java.io.*;

public class ZipFileUtils {
	/*
	 * 能支持中文的压缩 参数base 开始为"" first 开始为true
	 */
	private static void zip(org.apache.tools.zip.ZipOutputStream out, File f,
			String base, boolean first) throws Exception {
		if (first) {
			if (f.isDirectory()) {
				out.putNextEntry(new org.apache.tools.zip.ZipEntry("/"));
				base = base + f.getName();
				first = false;
			} else
				base = f.getName();
		}
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			base = base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName(), first);
			}
		} else {
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

	/*
	 * 解压文件 unZip为解压路径
	 */
	private static void unZipFileByOpache(org.apache.tools.zip.ZipFile zipFile,
			String unZipRoot) throws Exception, IOException {
		java.util.Enumeration e = zipFile.getEntries();
		org.apache.tools.zip.ZipEntry zipEntry;
		while (e.hasMoreElements()) {
			zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
			InputStream fis = zipFile.getInputStream(zipEntry);
			if (zipEntry.isDirectory()) {
			} else {
				File file = new File(unZipRoot + File.separator
						+ zipEntry.getName());
				File parentFile = file.getParentFile();
				parentFile.mkdirs();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] b = new byte[1024];
				int len;
				while ((len = fis.read(b, 0, b.length)) != -1) {
					fos.write(b, 0, len);
				}
				fos.close();
				fis.close();
			}
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param zipFileName
	 *            输出压缩文件名
	 * @param inputFileName
	 *            要进行压缩的路径
	 * @throws Exception
	 */
	public static void ZipFile(String zipFileName, String inputFileName)
			throws Exception {
		org.apache.tools.zip.ZipOutputStream out = new org.apache.tools.zip.ZipOutputStream(
				new FileOutputStream(zipFileName));
		File inputFile = new File(inputFileName);
		zip(out, inputFile, "", false);
		System.out.println("zip done");
		out.close();
	}

	/**
	 * 解压缩文件
	 * 
	 * @param unZipFileName
	 *            要解压的文件
	 * @param unZipPath
	 *            解压后的路径
	 * @throws Exception
	 */
	public static void unZipFile(String unZipFileName, String unZipPath)
			throws Exception {
		org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(
				unZipFileName);
		unZipFileByOpache(zipFile, unZipPath);
		System.out.println("unZip Ok");
	}

	public static void main(String[] args) throws Exception {
		// 193486
		long beginTime = System.currentTimeMillis();
		ZipFile("D:/edudb_full.tar.gz.zip", "D:/edudb_full.tar.gz");
		System.out.println(System.currentTimeMillis() - beginTime);
		// unZipFile("d:/javaFile.zip","e:/");
	}
}
