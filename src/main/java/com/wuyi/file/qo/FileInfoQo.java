package com.wuyi.file.qo;

import com.wuyi.core.query.object.PageQo;

/**
 * 文件信息查询对象
 *
 * @author WuYi Automatic Code Generator
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年08月31日 15:21:01
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileInfoQo extends PageQo {

	/** 主键 */
	protected Long id;

	/**
	 * 主键
	 * 
	 * @return id 主键
	 * @date 2018年08月31日 15:21:01
	 * @since 1.0.0
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 主键
	 * 
	 * @param id
	 *            主键
	 * @date 2018年08月31日 15:21:01
	 * @since 1.0.0
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
