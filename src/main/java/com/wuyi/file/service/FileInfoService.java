package com.wuyi.file.service;

import java.util.List;

import com.wuyi.common.model.BatchDeleteOrUpdateDo;
import com.wuyi.file.dto.FileInfoDTO;
import com.wuyi.file.model.FileInfo;
import com.wuyi.file.qo.FileInfoQo;

/**
 * 文件信息服务接口
 *
 * @author WuYi Automatic Code Generator
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年08月31日 15:06:17
 * @version 1.0.0
 * @since 1.0.0
 */
public interface FileInfoService {

	/**
	 * 新增文件信息
	 * 
	 * @param fileInfo
	 * @return boolean 新增成功则为真，反之则为假
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	boolean save(FileInfo fileInfo);

	/**
	 * 批量新增文件信息
	 * 
	 * @param fileInfoList
	 * @return boolean 批量新增成功则为真，反之则为假
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	boolean batchSave(List<FileInfo> fileInfoList);

	/**
	 * 通过主键删除文件信息
	 * 
	 * @param fileInfo
	 * @return boolean 删除成功则为真，反之则为假
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	boolean deleteById(FileInfo fileInfo);

	/**
	 * 通过主键批量删除文件信息
	 * 
	 * @param batchQo
	 * @return boolean 批量删除成功则为真，反之则为假
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	boolean batchDeleteById(BatchDeleteOrUpdateDo<Long> batchDo);

	/**
	 * 通过主键更新文件信息
	 * 
	 * @param fileInfo
	 * @return boolean 更新成功则为真，反之则为假
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	boolean updateById(FileInfo fileInfo);

	/**
	 * 通过主键获取文件信息
	 * 
	 * @param id
	 * @return 指定编号的DTO对象
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	FileInfoDTO getById(Long id);

	/**
	 * 分页查询查询符合条件文件信息
	 * 
	 * @param pageQo
	 *            查询对象
	 * @return 符合条件的DTO集合
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	List<FileInfoDTO> listByPage(FileInfoQo pageQo);

	/**
	 * 查询符合条件的文件信息数量
	 * 
	 * @param pageQo
	 *            查询对象
	 * @return 符合条件的对象数量
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	int count(FileInfoQo pageQo);

	/**
	 * 通过唯一识别符获取文件信息
	 * 
	 * @param uniqueId
	 * @return 指定编号的DTO对象
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	FileInfoDTO getByUniqueId(String uniqueId);
}
