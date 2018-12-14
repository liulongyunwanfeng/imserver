package org.springframework.web.multipart.cos;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;

public class ServerFilenameUtils {
	// The prefix used for temporary files
	private static final String VIDEO_PREFIX = "v_";

	public static String getVideoFilePath(String filename) {
		String name = FilenameUtils.getBaseName(filename);
		String ext = '.' + FilenameUtils.getExtension(filename);
		String filepath = StringUtils.replace(
				replaceChars(name, '-', File.separatorChar, 3), "-",
				File.separatorChar + VIDEO_PREFIX);
		return File.separatorChar + filepath + ext;
	}

	private static String replaceChars(String text, char oldChar, char newChar,
			int max) {
		if (StringUtils.isEmpty(text) || max == 0) {
			return text;
		}
		if (oldChar != newChar) {
			int len = text.length();
			int i = -1;
			char[] val = text.toCharArray();
			int off = 0;
			while (++i < len) {
				if (val[off + i] == oldChar) {
					break;
				}
			}
			if (i < len) {
				char buf[] = new char[len];
				for (int j = 0; j < i; j++) {
					buf[j] = val[off + j];
				}
				int count = 0;
				while (i < len) {
					char c = val[off + i];
					if ((count < max) && (c == oldChar)) {
						buf[i] = newChar;
						count++;
					} else {
						buf[i] = c;
					}
					i++;
				}
				return new String(buf, 0, len);
			}
		}
		return text;
	}
}