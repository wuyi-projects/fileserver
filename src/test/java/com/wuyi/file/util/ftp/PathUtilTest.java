/**
 * 
 * @date 2018年8月29日下午9:42:18
 * @version 1.0.0
 * @since 1.0.0 
 */
package com.wuyi.file.util.ftp;

import java.util.UUID;

import org.junit.Test;

import com.wuyi.file.util.PathUtil;

/**
 * @author WuYi Technology
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年8月29日下午9:42:18
 * @version 1.0.0
 * @since 1.0.0
 */
public class PathUtilTest {

	/**
	 * Test method for
	 * {@link com.wuyi.file.util.PathUtil#generateFTPDirectoryString(java.lang.String)}
	 * .
	 */
	@Test
	public void testGenerateDirectory() {

		for (int i = 0; i < 100; i++) {
			String name = UUID.randomUUID().toString();
			System.out.println(PathUtil.generateFTPDirectoryString(name));
		}
	}

}
