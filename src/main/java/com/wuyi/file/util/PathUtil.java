/**
 * 
 * @date 2018年8月29日上午7:43:53
 * @version 1.0.0
 * @since 1.0.0 
 */
package com.wuyi.file.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wuyi.util.Constants;

/**
 * @author WuYi Technology
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年8月29日上午7:43:53
 * @version 1.0.0
 * @since 1.0.0
 */
public class PathUtil {

	/**
	 * hash目录
	 * 
	 * @param filename
	 *            文件名
	 * @return 存储目录
	 * @date 2018年8月29日下午5:34:08
	 * @author SuperKoala
	 * @since 1.0.0
	 */
	public static String generateFTPDirectoryString(String filename) {
		int hashcode = filename.hashCode();
		int dir1 = (hashcode & 0x3FF);
		int dir2 = ((hashcode) & 0xFFC00) >> 10;
		// 构造新的保存目录
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.SEPARATOR_FTP);
		sb.append(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		sb.append(Constants.SEPARATOR_FTP);
		sb.append(tenTo36(dir1));
		sb.append(Constants.SEPARATOR_FTP);
		sb.append(tenTo36(dir2));
		return sb.toString();
	}

	/**
	 * hash目录
	 * 
	 * @param filename
	 *            文件名
	 * @return 存储目录
	 * @date 2018年8月29日下午5:34:08
	 * @author SuperKoala
	 * @since 1.0.0
	 */
	public static String generateDirectoryString(String filename) {
		int hashcode = filename.hashCode();
		int dir1 = (hashcode & 0x3FF);
		int dir2 = ((hashcode) & 0xFFC00) >> 10;
		// 构造新的保存目录
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.SEPARATOR_COMMON_DIRECTORY);
		sb.append(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		sb.append(Constants.SEPARATOR_COMMON_DIRECTORY);
		sb.append(tenTo36(dir1));
		sb.append(Constants.SEPARATOR_COMMON_DIRECTORY);
		sb.append(tenTo36(dir2));
		return sb.toString();
	}

	public static String generateDirectory(String root, String filename) {
		String path = generateDirectoryString(filename);
		makeMultiLevelDirectory(new File(root + path));
		return path;
	}

	public static void makeMultiLevelDirectory(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			makeMultiLevelDirectory(file.getParentFile());
			file.mkdir();
		}
	}

	private static final String[] X36_ARRAY = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
			"r", "s", "t", "u", "v", "w", "x", "y", "z" };

	private static String tenTo36(final int num) {
		int temp = num;
		StringBuilder sBuffer = new StringBuilder();
		while (temp > 0) {
			sBuffer.append(X36_ARRAY[temp % 36]);
			temp = temp / 36;
		}
		if (num == 0) {
			sBuffer.append("00");
		} else if (num < 36) {
			sBuffer.append("0");
		}
		return sBuffer.reverse().toString();
	}

}
