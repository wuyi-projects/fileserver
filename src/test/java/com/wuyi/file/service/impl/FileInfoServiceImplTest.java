package com.wuyi.file.service.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wuyi.common.model.BatchDeleteOrUpdateDo;
import com.wuyi.file.dto.FileInfoDTO;
import com.wuyi.file.model.FileInfo;
import com.wuyi.file.qo.FileInfoQo;
import com.wuyi.file.service.FileInfoService;

/**
 * 文件信息单元测试
 *
 * @author WuYi Automatic Code Generator
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年08月31日 15:21:01
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-*.xml" })
public class FileInfoServiceImplTest {

	@Autowired
	private FileInfoService fileInfoService;

	/**
	 * Test method for
	 * {@link com.wuyi.file.service.impl.FileInfoServiceImpl#save(com.wuyi.file.model.FileInfo)}
	 * .
	 */
	@Test
	public void testSave() {
		FileInfo model = new FileInfo();
		model.setUniqueId("test");
		model.setPath("test");
		model.setType("test");
		model.setFingerprint("test");
		model.setSize(100L);
		// 实体类赋值
		boolean r = fileInfoService.save(model);
		assertTrue(r);
	}

	/**
	 * Test method for
	 * {@link com.wuyi.file.service.impl.FileInfoServiceImpl#batchSave(java.util.List)}
	 * .
	 */
	@Test
	public void testBatchSave() {
		List<FileInfo> list = new ArrayList<FileInfo>();
		int times = 100;
		FileInfo model;
		for (int i = 0; i < times; i++) {
			model = new FileInfo();
			// 实体类赋值
			model.setUniqueId("test");
			model.setPath("test" + i);
			model.setType("test" + i);
			model.setFingerprint("test" + i);
			model.setSize(1000L * i);
			list.add(model);
		}
		boolean r = fileInfoService.batchSave(list);
		assertTrue(r);
	}

	/**
	 * Test method for
	 * {@link com.wuyi.file.service.impl.FileInfoServiceImpl#deleteById(com.wuyi.file.model.FileInfo)}
	 * .
	 */
	@Test
	public void testDeleteById() {
		FileInfo model = new FileInfo();
		model.setId(60237069422592L);
		boolean r = fileInfoService.deleteById(model);
		assertTrue(r);
	}

	/**
	 * Test method for
	 * {@link com.wuyi.file.service.impl.FileInfoServiceImpl#batchDeleteById(com.wuyi.common.model.BatchDeleteOrUpdateDo)}
	 * .
	 */
	@Test
	public void testBatchDeleteById() {
		BatchDeleteOrUpdateDo<Long> batchDo = new BatchDeleteOrUpdateDo<Long>();
		List<Long> list = new ArrayList<Long>();
		list.add(60237085151232L);
		list.add(60237085151233L);
		list.add(60237085151234L);
		list.add(60237085151235L);
		batchDo.setList(list);
		boolean r = fileInfoService.batchDeleteById(batchDo);
		assertTrue(r);
	}

	/**
	 * Test method for
	 * {@link com.wuyi.file.service.impl.FileInfoServiceImpl#updateById(com.wuyi.file.model.FileInfo)}
	 * .
	 */
	@Test
	public void testUpdateById() {
		FileInfo model = new FileInfo();
		model.setId(60237085151236L);
		model.setUniqueId("999");
		model.setPath("999");
		model.setType("999");
		model.setFingerprint("999");
		model.setSize(999L);
		// 实体类赋值
		boolean r = fileInfoService.updateById(model);
		assertTrue(r);
	}

	/**
	 * Test method for
	 * {@link com.wuyi.file.service.impl.FileInfoServiceImpl#getById(java.lang.Long)}
	 * .
	 */
	@Test
	public void testGetById() {
		Long id = 60237085151236L;
		FileInfoDTO dto = fileInfoService.getById(id);
		System.out.println(dto);
		assertTrue(dto != null);
	}

	/**
	 * Test method for
	 * {@link com.wuyi.file.service.impl.FileInfoServiceImpl#listByPage(com.wuyi.file.qo.FileInfoQo)}
	 * .
	 */
	@Test
	public void testListByPage() {
		FileInfoQo pageQo = new FileInfoQo();
		pageQo.setOffset(0);
		pageQo.setLimit(10);
		List<FileInfoDTO> list = fileInfoService.listByPage(pageQo);
		assertTrue(list != null);
	}

	/**
	 * Test method for
	 * {@link com.wuyi.file.service.impl.FileInfoServiceImpl#count(com.wuyi.file.qo.FileInfoQo)}
	 * .
	 */
	@Test
	public void testCount() {
		FileInfoQo pageQo = new FileInfoQo();
		int result = fileInfoService.count(pageQo);
		System.out.println(result);
	}

	@Test
	public void testGetByUniqueId() {
		String uniqueId = "878660a7-50e1-4281-8a0c-8d93590362b2";
		FileInfoDTO dto = fileInfoService.getByUniqueId(uniqueId);
		System.out.println(dto);
		assertTrue(dto != null);
	}
}
