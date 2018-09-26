package com.wuyi.file.dto.converter;

import java.util.List;

import com.wuyi.core.dto.util.AbstractBeanConverter;
import com.wuyi.file.dto.FileInfoDTO;
import com.wuyi.file.model.FileInfo;

/**
 * 文件信息转换类
 *
 * @author WuYi Automatic Code Generator
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年08月31日 15:06:17
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileInfoConverter {


	/**
	 * 文件信息Model-->文件信息DTO
	 * 
	 * @param model object
	 * @return DTO object
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	public static FileInfoDTO toDto(FileInfo model) {
		return new ToDtoConverter().toBean(model);
	}

	/**
	 * 文件信息Model集合-->文件信息DTO集合
	 * 
	 * @param DTO list
	 * @return model list
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	public static List<FileInfoDTO> toDtoList(List<FileInfo> modelList) {
		return new ToDtoConverter().toBeanList(modelList);
	}

	/**
	 * 文件信息DTO-->文件信息Model
	 * 
	 * @param modelDto
	 * @return model object
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	public static FileInfo fromDto(FileInfoDTO modelDto) {
		return new ToConverter().toBean(modelDto);
	}

	/**
	 * 文件信息DTO集合-->文件信息Model集合
	 * 
	 * @param DTO list
	 * @return model list
	 * @date 2018年08月31日 15:06:17
	 * @since 1.0.0
	 */
	public static List<FileInfo> fromDtoList(List<FileInfoDTO> modelList) {
		return new ToConverter().toBeanList(modelList);
	}

	/** 文件信息Model-->文件信息DTO */
	static class ToDtoConverter extends AbstractBeanConverter<FileInfo, FileInfoDTO> {
	}

	/** 文件信息DTO-->文件信息Model */
	static class ToConverter extends AbstractBeanConverter<FileInfoDTO, FileInfo> {
	}

}