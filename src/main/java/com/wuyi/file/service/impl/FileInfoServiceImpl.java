package com.wuyi.file.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.wuyi.common.model.BatchDeleteOrUpdateDo;
import com.wuyi.file.dto.FileInfoDTO;
import com.wuyi.file.model.FileInfo;
import com.wuyi.file.qo.FileInfoQo;
import com.wuyi.file.service.FileInfoService;
import com.wuyi.util.sequence.IUniqueSequenceGenerator;
import com.wuyi.util.sequence.UniqueSequenceGeneratorFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.wuyi.core.model.util.DoUtil;
import com.wuyi.core.service.impl.BaseServiceImpl;

/**
 * 文件信息服务实现类
 *
 * @author 张自强
 * @company 武汉东日科技有限公司
 *
 * @date 2018年08月31日 15:06:17
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fileInfoService")
public class FileInfoServiceImpl extends BaseServiceImpl<FileInfo, Long> implements FileInfoService {

	/** 序列号生成器 */
	private static IUniqueSequenceGenerator idGenerator;

	static {
		idGenerator = UniqueSequenceGeneratorFactory.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wuyi.file.service.FileInfoService#save(com.wuyi.file.model.FileInfo)
	 */
	@Override
	public boolean save(FileInfo fileInfo) {
		if (fileInfo == null) {
			return false;
		}
		fileInfo = (FileInfo) DoUtil.forCreate(fileInfo);
		fileInfo.setId(idGenerator.getUniqueId());
		int result = baseDao.save("FileInfoModel.save", fileInfo);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wuyi.file.service.FileInfoService#batchSave(java.util.List)
	 */
	@Override
	public boolean batchSave(List<FileInfo> fileInfoList) {
		if (fileInfoList == null) {
			return false;
		}
		// 大数据量分批提交
		fileInfoList = DoUtil.forCreateList(fileInfoList);
		for (FileInfo fileInfo : fileInfoList) {
			fileInfo.setId(idGenerator.getUniqueId());
		}
		int result = baseDao.save("FileInfoModel.batchBave", fileInfoList);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wuyi.file.service.FileInfoService#deleteById(com.wuyi.file.model.
	 * FileInfo)
	 */
	@Override
	public boolean deleteById(FileInfo fileInfo) {
		if (fileInfo == null) {
			return false;
		}
		fileInfo = DoUtil.forDelete(fileInfo);
		int result = baseDao.update("FileInfoModel.deleteById", fileInfo);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wuyi.file.service.FileInfoService#batchDeleteById(com.wuyi.common
	 * .model.BatchDeleteOrUpdateDo)
	 */
	@Override
	public boolean batchDeleteById(BatchDeleteOrUpdateDo<Long> batchDo) {
		if (batchDo == null) {
			return false;
		}
		Timestamp updateTime = new Timestamp(new Date().getTime());
		batchDo.setUpdateTime(updateTime);
		int result = baseDao.update("FileInfoModel.batchDeleteById", batchDo);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wuyi.file.service.FileInfoService#updateById(FileInfo)
	 */
	@Override
	public boolean updateById(FileInfo fileInfo) {
		if (fileInfo == null) {
			return false;
		}
		fileInfo = DoUtil.forUpdate(fileInfo);
		int result = baseDao.update("FileInfoModel.updateById", fileInfo);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wuyi.file.service.FileInfoService#getById(java.lang.Long)
	 */
	@Override
	public FileInfoDTO getById(Long id) {
		if (id == null) {
			return null;
		}
		FileInfoDTO fileInfoDTO = (FileInfoDTO) baseDao.getObject("FileInfoModel.getById", id);
		return fileInfoDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wuyi.file.service.FileInfoService#listByPage(com.wuyi.file.qo.FileInfoQo
	 * )
	 */
	@Override
	public List<FileInfoDTO> listByPage(FileInfoQo pageQo) {
		if (pageQo == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<FileInfoDTO> list = (List<FileInfoDTO>) baseDao.listObject("FileInfoModel.listByPage",
				pageQo, pageQo.getOffset(), pageQo.getLimit());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wuyi.file.service.FileInfoService#count(com.wuyi.file.qo.FileInfoQo)
	 */
	@Override
	public int count(FileInfoQo pageQo) {
		return baseDao.count("FileInfoModel.count", pageQo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wuyi.file.service.FileInfoService#getByUniqueId(java.lang.Long)
	 */
	@Override
	public FileInfoDTO getByUniqueId(String uniqueId) {
		if (StringUtils.isBlank(uniqueId)) {
			return null;
		}
		FileInfoDTO fileInfoDTO = (FileInfoDTO) baseDao.getObject("FileInfoModel.getByUniqueId",
				uniqueId);
		return fileInfoDTO;
	}
}
