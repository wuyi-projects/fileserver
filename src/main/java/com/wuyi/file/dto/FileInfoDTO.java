package com.wuyi.file.dto;

import com.wuyi.core.dto.BaseDTO;

/**
 * 文件信息传输对象
 *
 * @author WuYi Automatic Code Generator
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年08月31日 15:06:17
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileInfoDTO extends BaseDTO<Long> {

	/** 默认UID */
	private static final long serialVersionUID = 1677547462844529173L;

	/** 唯一识别符 */
	private String uniqueId;

	/** 路径 */
	private String path;

	/** 文件类型 */
	private String type;

	/** 指纹 */
	private String fingerprint;

	/** 大小 */
	private Long size;

	/**
	 * 唯一识别符
	 * 
	 * @return uniqueId 唯一识别符
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * 唯一识别符
	 * 
	 * @param uniqueId
	 *            唯一识别符
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId == null ? null : uniqueId.trim();
	}

	/**
	 * 路径
	 * 
	 * @return path 路径
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 路径
	 * 
	 * @param path
	 *            路径
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public void setPath(String path) {
		this.path = path == null ? null : path.trim();
	}

	/**
	 * 文件类型
	 * 
	 * @return type 文件类型
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public String getType() {
		return type;
	}

	/**
	 * 文件类型
	 * 
	 * @param type
	 *            文件类型
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	/**
	 * 指纹
	 * 
	 * @return fingerprint 指纹
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public String getFingerprint() {
		return fingerprint;
	}

	/**
	 * 指纹
	 * 
	 * @param fingerprint
	 *            指纹
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint == null ? null : fingerprint.trim();
	}

	/**
	 * 大小
	 * 
	 * @return size 大小
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * 大小
	 * 
	 * @param size
	 *            大小
	 * @date 2018年08月31日14:21:00
	 * @since 1.0.0
	 */
	public void setSize(Long size) {
		this.size = size;
	}
}