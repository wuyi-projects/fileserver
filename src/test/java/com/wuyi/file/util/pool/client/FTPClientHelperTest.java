/**
 * 
 * @date 2018年8月27日下午2:36:41
 * @version 1.0.0
 * @since 1.0.0 
 */
package com.wuyi.file.util.pool.client;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wuyi.file.util.ftp.pool.client.FTPClientHelper;

/**
 * @author WuYi Technology
 * @company 物一（武汉）网络技术有限公司
 *
 * @date 2018年8月27日下午2:36:41
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-*.xml" })
public class FTPClientHelperTest {

	@Autowired
	private FTPClientHelper ftpClientHelper;

	@Test
	public void test() {
		boolean r = false;
		try {
			r = ftpClientHelper.makeDirectory("/vzzvz");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(r);
	}

	@Test
	public void crateDir() {
		String dir = "aa/bb";
		boolean success = ftpClientHelper.createDir(dir);
		System.out.print(success ? "成功" : "失败");
		System.out.println(dir);

		dir = "aa/bb/c1";
		success = ftpClientHelper.createDir(dir);
		System.out.print(success ? "成功" : "失败");
		System.out.println(dir);

		dir = "aa/bb/中国";
		success = ftpClientHelper.createDir(dir);
		System.out.print(success ? "成功" : "失败");
		System.out.println(dir);

		assertTrue(success);
	}

	@Test
	public void downloadFile() {
		String ftpDirName = "/aa/bb";
		String ftpFileName = "IQIYIsetup_z22.exe";
		String localFileFullName = "D:/0/down/IQIYIsetup_z22.exe";
		boolean result = ftpClientHelper.downloadFile(ftpDirName, ftpFileName, localFileFullName);
		assertTrue(result);

//		ftpDirName = "/temp";
//		ftpFileName = "文本.txt";
//		localFileFullName = "D:/0/down/文本.txt";
//		result = ftpClientHelper.downloadFile(ftpDirName, ftpFileName, localFileFullName);
//		assertTrue(result);
	}

	@Test
	public void testUplodaFile() {
		String localFile = "D:/0/IQIYIsetup_z22.exe";
		String ftpDir = "/aa/bb";
		String ftpFile = "IQIYIsetup_z22.exe";
		boolean success = ftpClientHelper.uploadFile(localFile, ftpDir, ftpFile);
		assertTrue(success);

//		localFile = "D:/0/文本.txt";
//		ftpDir = "/aa/bb/c2";
//		ftpFile = "文本.txt";
//		success = ftpClientHelper.uploadFile(localFile, ftpDir, ftpFile);
		assertTrue(success);

	}

	@Test
	public void removeDir() {
		String dir = "aa/bb";
		boolean success = ftpClientHelper.removeDir(dir);
		System.out.print(success ? "成功" : "失败");
		System.out.println(dir);

		dir = "aa/bb/c1";
		success = ftpClientHelper.removeDir(dir);
		System.out.print(success ? "成功" : "失败");
		System.out.println(dir);

		dir = "aa/bb/c2";
		success = ftpClientHelper.removeDir(dir);
		System.out.print(success ? "成功" : "失败");
		System.out.println(dir);

		dir = "/aa/bb/中国";
		success = ftpClientHelper.removeDir(dir);
		System.out.print(success ? "成功" : "失败");
		System.out.println(dir);

		assertTrue(success);
	}

	@Test
	public void testRemove() {
		boolean success = false;
		String file = "/aa/bb/img.jpg";
		success = ftpClientHelper.removeFile(file);

		assertTrue(success);
	}

	@Test
	public void testUplodFileStream() {

		try {
			String localFile = "D:/0/img.jpg";
			String ftpDir = "temp";
			String ftpFile = "img.jpg";

			FileInputStream fi = new FileInputStream(localFile);

			boolean success = ftpClientHelper.uploadFile(fi, ftpDir, ftpFile);
			assertTrue(success);

			localFile = "D:/0/文本.txt";
			ftpDir = "temp";
			ftpFile = "文本.txt";

			fi = new FileInputStream(localFile);
			success = ftpClientHelper.uploadFile(fi, ftpDir, ftpFile);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
