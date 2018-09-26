package com.wuyi.file.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyi.common.dto.MultiKeyDTO;
import com.wuyi.common.model.BatchDeleteOrUpdateDo;
import com.wuyi.core.controller.BaseController;
import com.wuyi.file.dto.FileInfoDTO;
import com.wuyi.file.dto.converter.FileInfoConverter;
import com.wuyi.file.model.FileInfo;
import com.wuyi.file.qo.FileInfoQo;
import com.wuyi.file.service.FileInfoService;
import com.wuyi.util.format.json.BootstrapTableJson;
import com.wuyi.util.format.json.RequestJson;
import com.wuyi.util.format.json.StatusEnum;

/**
 * 文件信息控制器
 *
 * @author WuYi Automatic Code Generator
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年08月31日 15:06:18
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
@RequestMapping("/fileInfo")
public class FileInfoController extends BaseController {
	
	@Autowired
	@Qualifier("fileInfoService")
	private FileInfoService fileInfoService;

	/** 新增文件信息 */
	@RequestMapping("save.json")
	@ResponseBody
	public Map<String, Object> save(@RequestBody FileInfoDTO fileInfoDTO) {
		
		boolean r = false;
		if (fileInfoDTO != null) {
			FileInfo fileInfo = FileInfoConverter.fromDto(fileInfoDTO);
			r = fileInfoService.save(fileInfo);
		}
		return RequestJson.pack(StatusEnum.SUCCEESS, r);
	}

	/** 删除文件信息 */
	@RequestMapping("delete.json")
	@ResponseBody
	public Map<String, Object> delete(@RequestBody FileInfoDTO fileInfoDTO) {
		
		FileInfo fileInfo = FileInfoConverter.fromDto(fileInfoDTO);
		boolean r = fileInfoService.deleteById(fileInfo);
		return RequestJson.pack(StatusEnum.SUCCEESS, r);
	}

	/** 批量删除文件信息 */
	@RequestMapping("batchDelete.json")
	@ResponseBody
	public Map<String, Object> batchDelete(@RequestBody MultiKeyDTO<Long> multiKeyDTO) {
		
		boolean r = false;
		if (multiKeyDTO != null && multiKeyDTO.getIds() != null) {
			Long updateUserId = 100L;
			BatchDeleteOrUpdateDo<Long> batchDo = new BatchDeleteOrUpdateDo<Long>();
			batchDo.setUpdateUserId(updateUserId);
			Timestamp updateTime = new Timestamp(new Date().getTime());
			batchDo.setUpdateTime(updateTime);
			batchDo.setList(multiKeyDTO.getIds());
			r = fileInfoService.batchDeleteById(batchDo);
		}
		return RequestJson.pack(StatusEnum.SUCCEESS, r);
	}

	/** 编辑文件信息 */
	@RequestMapping("edit.json")
	@ResponseBody
	public Map<String, Object> edit(@RequestBody FileInfoDTO fileInfoDTO) {
		
		boolean r = false;
		if (fileInfoDTO != null) {
			FileInfo fileInfo = FileInfoConverter.fromDto(fileInfoDTO);
			r = fileInfoService.updateById(fileInfo);
		}
		return RequestJson.pack(StatusEnum.SUCCEESS, r);
	}

	/** 主键查找文件信息 */
	@RequestMapping("get.json")
	@ResponseBody
	public Map<String, Object> getById(@RequestBody FileInfoQo fileInfoQo) {
		
		FileInfoDTO fileInfoDTO = fileInfoService.getById(fileInfoQo.getId());
		return RequestJson.pack(StatusEnum.SUCCEESS, fileInfoDTO);
	}

	/** 批量查找文件信息 */
	@RequestMapping("list.json")
	@ResponseBody
	public Map<String, Object> list(@RequestBody FileInfoQo pageQo) {

		List<FileInfoDTO> dtoList = fileInfoService.listByPage(pageQo);
		int count = fileInfoService.count(pageQo);
		int offset = pageQo.getOffset();
		int pageSize = pageQo.getLimit();
		int page = offset % pageSize == 0 ? offset / pageSize : (offset / pageSize + 1);
		return BootstrapTableJson.pack(StatusEnum.SUCCEESS, page, count, dtoList);
	}
	
}
