/**
 * 
 * @date 2018年8月31日上午11:15:09
 * @version 1.0.0
 * @since 1.0.0 
 */
package com.wuyi.file.util.file;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author WuYi Technology
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年8月31日上午11:15:09
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileTypeUtil {

	public static FileType getFileType(InputStream is) throws IOException {
		byte[] src = new byte[28];
		is.read(src, 0, 28);
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v).toUpperCase();
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		FileType[] fileTypes = FileType.values();
		for (FileType fileType : fileTypes) {
			if (stringBuilder.toString().startsWith(fileType.getValue())) {
				return fileType;
			}
		}
		return null;
	}

}
