/**
 * 
 * @date 2018年4月10日下午1:14:26
 * @version 1.0.0
 * @since 1.0.0 
 */
package com.wuyi.file.constant;

import java.util.Properties;

import com.wuyi.util.PropertiesUtil;

/**
 * @author 张自强
 * @company 武汉东日科技有限公司
 *
 * @date 2018年4月10日下午1:14:26
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileServerPathConst {

	/** 模板配置文件 */
	static final Properties FILE_SERVER_PATH_CONFIG = PropertiesUtil
			.loadProperties("file-server.properties");
	
	/** 文件根目录 */
	public static final String FILE_SERVER_ROOT_PATH = String.valueOf(FILE_SERVER_PATH_CONFIG
			.getProperty("file.server.root.path"));
	
	/** 图片根路径 */
	public static final String IMAGE_SERVER_ROOT_PATH = String.valueOf(FILE_SERVER_PATH_CONFIG
			.getProperty("image.server.root.path"));


}
