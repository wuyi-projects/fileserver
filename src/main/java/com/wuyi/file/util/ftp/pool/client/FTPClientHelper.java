package com.wuyi.file.util.ftp.pool.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.wuyi.file.util.PathUtil;
import com.wuyi.file.util.ftp.pool.core.FTPClientPool;
import com.wuyi.file.util.ftp.pool.util.ByteUtil;
import com.wuyi.util.Constants;

/**
 * ftp客户端辅助bean
 * 
 * @author jelly
 *
 */
public class FTPClientHelper {

	private static Logger LOGGER = LoggerFactory.getLogger(FTPClientHelper.class);

	private static final String CHARSET_UTF8 = "UTF-8";
	private static final String CHARSET_DEFAULT = "iso-8859-1";

	private FTPClientPool ftpClientPool;

	public void setFtpClientPool(FTPClientPool ftpClientPool) {
		this.ftpClientPool = ftpClientPool;
	}

	/**
	 * 下载remote文件流
	 * 
	 * @param remote
	 *            远程文件
	 * @return 字节数据
	 * @throws Exception
	 */
	public byte[] retrieveFileStream(String remote) throws Exception {
		FTPClient client = null;
		InputStream in = null;
		try {
			long start = System.currentTimeMillis();
			client = ftpClientPool.borrowObject();
			in = client.retrieveFileStream(remote);

			long end = System.currentTimeMillis();
			System.out.println("ftp下载耗时(毫秒):" + (end - start));

			if (in != null) {
				return ByteUtil.inputStreamToByteArray(in);
			} else {
				return new byte[0];
			}

		} catch (Exception e) {
			LOGGER.error("获取ftp下载流异常", e);
		} finally {
			if (in != null) {
				in.close();
			}
			if (client != null) {
				client.logout();
				client.disconnect();
			}
			ftpClientPool.returnObject(client);
		}
		return null;
	}

	/**
	 * 创建目录 单个不可递归
	 * 
	 * @param pathname
	 *            目录名称
	 * @return
	 * @throws Exception
	 */
	public boolean makeDirectory(String pathname) throws Exception {

		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			return client.makeDirectory(pathname);
		} catch (Exception e) {
			LOGGER.error("创建目录失败", e);
			throw e;
		} finally {
			ftpClientPool.returnObject(client);
		}
	}

	/**
	 * 删除目录，单个不可递归
	 * 
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	public boolean removeDirectory(String pathname) throws Exception {
		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			return client.removeDirectory(pathname);
		} catch (Exception e) {
			LOGGER.error("删除目录失败", e);
			throw e;
		} finally {
			ftpClientPool.returnObject(client);
		}
	}

	/**
	 * 删除文件 单个 ，不可递归
	 * 
	 * @param pathname
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFile(String pathname) throws Exception {

		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			return client.deleteFile(pathname);
		} catch (Exception e) {
			LOGGER.error("创建文件失败", e);
			throw e;
		} finally {
			ftpClientPool.returnObject(client);
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param remote
	 * @param local
	 * @return
	 * @throws Exception
	 */
	public boolean storeFile(String remote, InputStream local) throws Exception {
		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			return client.storeFile(remote, local);
		} catch (Exception e) {
			LOGGER.error("上传文件失败", e);
			throw e;
		} finally {
			ftpClientPool.returnObject(client);
		}
	}

	public boolean uploadFile(MultipartFile[] uploadFile, String basePath, String filePath) {
		try {
			FTPClient client = ftpClientPool.borrowObject();

			if (!client.changeWorkingDirectory(basePath + filePath)) {
				// 如果目录不存在创建目录
				String[] dirs = filePath.split("/");
				String tempPath = basePath;
				for (String dir : dirs) {
					if (null == dir || "".equals(dir))
						continue;
					tempPath += "/" + dir;
					if (!client.changeWorkingDirectory(tempPath)) {
						if (!client.makeDirectory(tempPath)) {
							return false;
						} else {
							client.changeWorkingDirectory(tempPath);
						}
					}
				}
			}

			if (uploadFile != null && uploadFile.length > 0) {
				for (int i = 0; i < uploadFile.length; i++) {
					MultipartFile file = uploadFile[i];
					saveFile(file, client);
				}
			}
			ftpClientPool.returnObject(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public String uploadFile(MultipartFile uploadFile) throws Exception {

		String originalFilename = uploadFile.getOriginalFilename();
		String fileName = UUID.randomUUID().toString() + Constants.SEPARATOR_DOT
				+ StringUtils.split(originalFilename, Constants.SEPARATOR_DOT)[1];
		String path = PathUtil.generateFTPDirectoryString(fileName);
		String fullPath = null;
		try {
			FTPClient client = ftpClientPool.borrowObject();

			// if (!client.changeWorkingDirectory(path)) {
			// // 如果目录不存在创建目录
			// String[] dirs = path.split("/");
			// String tempPath = path;
			// for (String dir : dirs) {
			// if (StringUtils.isBlank(dir)) {
			// continue;
			// }
			// tempPath += "/" + dir;
			// if (!client.changeWorkingDirectory(tempPath)) {
			// if (!client.makeDirectory(tempPath)) {
			// return fullPath;
			// } else {
			// client.changeWorkingDirectory(tempPath);
			// }
			// }
			// }
			// }
			makeAndChangeWorkingDirectory(client, path);
			if (uploadFile != null) {
				boolean isSuccess = saveFile(uploadFile, client);
				if (isSuccess) {
					fullPath = path + Constants.SEPARATOR_FTP + fileName;
					return fullPath;
				}
			}
			ftpClientPool.returnObject(client);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("文件上传失败，失败原因{}", e);
			throw new Exception(e);
		}
		return fullPath;
	}

	public static void makeAndChangeWorkingDirectory(FTPClient client, String path)
			throws IOException {
		if (client == null && path == null) {
			return;
		}
		String[] dirs = StringUtils.split(path, Constants.SEPARATOR_FTP);
		makeAndChangeWorkingDirectory(client, dirs);
	}

	public static void makeAndChangeWorkingDirectory(FTPClient client, String[] path)
			throws IOException {
		if (client == null && path == null) {
			return;
		}
		int level = path.length;
		String rootPath = path[0];
		if (level <= 1) {
			if (StringUtils.isNotBlank(rootPath)) {
				if (!client.changeWorkingDirectory(rootPath)) {
					if (!client.makeDirectory(rootPath)) {
					} else {
						client.changeWorkingDirectory(rootPath);
					}
				}
			}
		} else {
			if (StringUtils.isNotBlank(rootPath)) {
				if (!client.changeWorkingDirectory(rootPath)) {
					if (!client.makeDirectory(rootPath)) {
					} else {
						client.changeWorkingDirectory(rootPath);
					}
				}
			}
			String[] nextPath = new String[level - 1];
			System.arraycopy(path, 1, nextPath, 0, level - 1);
			makeAndChangeWorkingDirectory(client, nextPath);
		}
	}

	private boolean saveFile(MultipartFile file, FTPClient client) {
		boolean success = false;
		InputStream inStream = null;
		try {
			String fileName = new String(file.getOriginalFilename());
			inStream = file.getInputStream();
			success = client.storeFile(fileName, inStream);
			if (success == true) {
				return success;
			}
		} catch (Exception e) {
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}
		}
		return success;
	}

	public boolean downloadFile(HttpServletResponse response, String fullPath) {
		if (StringUtils.isBlank(fullPath)) {
			return false;
		}
		int index = fullPath.lastIndexOf(Constants.SEPARATOR_FTP);
		String fileName;
		String path;
		if (index == -1) {
			path = Constants.SEPARATOR_FTP;
			fileName = fullPath;
		} else {
			path = fullPath.substring(0, index);
			fileName = fullPath.substring(index + 1, fullPath.length());
		}
		return downloadFile(response, fileName, path);
	}

	public boolean downloadFile(HttpServletResponse response, String fileName, String path) {
		response.setCharacterEncoding(CHARSET_UTF8);
		response.setContentType("multipart/form-data;charset=" + CHARSET_UTF8);
		try {
			FTPClient client = ftpClientPool.borrowObject();
			client.changeWorkingDirectory(path);
			FTPFile[] fs = client.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					response.setHeader("Content-Disposition", "attachment;fileName="
							+ new String(ff.getName().getBytes(CHARSET_UTF8), CHARSET_DEFAULT));
					OutputStream os = response.getOutputStream();
					client.retrieveFile(ff.getName(), os);
					os.flush();
					os.close();
					break;
				}
			}
			ftpClientPool.returnObject(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean getImage(HttpServletResponse response, String fileName, String path) {
		response.setCharacterEncoding(CHARSET_UTF8);
		response.setContentType("multipart/form-data;charset=" + CHARSET_UTF8);
		try {
			FTPClient client = ftpClientPool.borrowObject();
			client.changeWorkingDirectory(path);
			FTPFile[] fs = client.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					response.setHeader("Content-Disposition", "attachment;fileName="
							+ new String(ff.getName().getBytes(CHARSET_UTF8), CHARSET_DEFAULT));
					OutputStream os = response.getOutputStream();
					client.retrieveFile(ff.getName(), os);
					os.flush();
					os.close();
					break;
				}
			}
			ftpClientPool.returnObject(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 * ftp上传文件
	 * 
	 * @param localFileName
	 *            待上传文件
	 * @param ftpDirName
	 *            ftp 目录名
	 * @param ftpFileName
	 *            ftp目标文件
	 * @return true||false
	 */
	public boolean uploadFile(String localFileName, String ftpDirName, String ftpFileName) {
		return uploadFile(localFileName, ftpDirName, ftpFileName, false);
	}

	/**
	 * 
	 * ftp上传文件
	 * 
	 * @param localFileName
	 *            待上传文件
	 * @param ftpDirName
	 *            ftp 目录名
	 * @param ftpFileName
	 *            ftp目标文件
	 * @param deleteLocalFile
	 *            是否删除本地文件
	 * @return true||false
	 */
	public boolean uploadFile(String localFileName, String ftpDirName, String ftpFileName,
			boolean deleteLocalFile) {

		LOGGER.info("准备上传 [{}] 到 ftp://{}/{2}", localFileName, ftpDirName, ftpFileName);
		if (StringUtils.isBlank(ftpFileName)) {
			throw new RuntimeException("上传文件必须填写文件名！");

		}
		File srcFile = new File(localFileName);
		if (!srcFile.exists()) {
			throw new RuntimeException("文件不存在：" + localFileName);
		}
		try (FileInputStream fis = new FileInputStream(srcFile)) {
			// 上传文件
			boolean flag = uploadFile(fis, ftpDirName, ftpFileName);
			// 删除文件
			if (deleteLocalFile) {
				srcFile.delete();
				LOGGER.info("ftp删除源文件：{}", srcFile);
			}
			fis.close();
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
		}
	}

	/**
	 * 
	 * ftp上传文件 (使用inputstream)
	 * 
	 * @param localFileName
	 *            待上传文件
	 * @param ftpDirName
	 *            ftp 目录名
	 * @param ftpFileName
	 *            ftp目标文件
	 * @return true||false
	 */
	public boolean uploadFile(FileInputStream uploadInputStream, String ftpDirName,
			String ftpFileName) {

		LOGGER.info("准备上传 [流] 到 ftp://{}/{}", ftpDirName, ftpFileName);
		FTPClient client = null;
		if (StringUtils.isBlank(ftpFileName)) {
			throw new RuntimeException("上传文件必须填写文件名！");
		}
		try {
			// 设置上传目录(没有则创建)
			if (!createDir(ftpDirName)) {
				throw new RuntimeException("切入FTP目录失败：" + ftpDirName);
			}
			// 上传
			String fileName = new String(ftpFileName.getBytes(CHARSET_UTF8), CHARSET_DEFAULT);

			client = ftpClientPool.borrowObject();
			if (client.storeFile(fileName, uploadInputStream)) {
				uploadInputStream.close();
				LOGGER.info("文件上传成功：{}/{}", ftpDirName, ftpFileName);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("文件上传异常，异常信息：", e);
			return false;
		} finally {
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param ftpDirName
	 *            ftp目录名
	 * @param ftpFileName
	 *            ftp文件名
	 * @param localFileFullName
	 *            本地文件名
	 * @return
	 * @author xxj
	 */
	public boolean downloadFile(String ftpDirName, String ftpFileName, String localFileFullName) {
		FTPClient client = null;
		try {
			if ("".equals(ftpDirName))
				ftpDirName = File.separator;
			String dir = new String(ftpDirName.getBytes(CHARSET_UTF8), CHARSET_DEFAULT);

			client = ftpClientPool.borrowObject();
			if (!client.changeWorkingDirectory(dir)) {
				LOGGER.info("切换目录失败：{}", ftpDirName);
				return false;
			}
			FTPFile[] fs = client.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(ftpFileName)) {
					FileOutputStream is = new FileOutputStream(new File(localFileFullName));
					client.retrieveFile(ff.getName(), is);
					is.close();
					LOGGER.info("文件下载成功:{}", localFileFullName);
					return true;
				}
			}
			LOGGER.info("文件下载失败:{}" + File.pathSeparator + "{}", ftpDirName, ftpFileName);
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ftpClientPool.returnObject(client);
		}
	}

	/**
	 * 
	 * 删除ftp上的文件
	 * 
	 * @param ftpFileName
	 * @return true || false
	 */
	public boolean removeFile(String ftpFileName) {
		boolean flag = false;
		FTPClient client = null;
		LOGGER.info("待删除文件:{}", ftpFileName);
		try {
			ftpFileName = new String(ftpFileName.getBytes(CHARSET_UTF8), CHARSET_DEFAULT);

			client = ftpClientPool.borrowObject();
			flag = client.deleteFile(ftpFileName);
			LOGGER.info("删除文件{}:{}", flag ? "成功" : "失败", ftpFileName);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("删除文件失败:{}", ftpFileName);
			return false;
		} finally {
			ftpClientPool.returnObject(client);
		}

	}

	/**
	 * 删除空目录
	 * 
	 * @param dir
	 * @return
	 */
	public boolean removeDir(String dir) {
		if (StringUtils.indexOf(dir, "/") != 0) {
			dir = "/" + dir;
		}
		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			String d = new String(dir.toString().getBytes(CHARSET_UTF8), CHARSET_DEFAULT);
			return client.removeDirectory(d);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ftpClientPool.returnObject(client);
		}
	}

	/**
	 * 创建目录(有则切换目录，没有则创建目录)
	 * 
	 * @param dir
	 * @return
	 */
	public boolean createDir(String dir) {
		if (StringUtils.isBlank(dir)) {
			return true;
		}
		String d;
		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			// 目录编码，解决中文路径问题
			d = new String(dir.toString().getBytes(CHARSET_UTF8), CHARSET_DEFAULT);
			// 尝试切入目录
			if (client.changeWorkingDirectory(d)) {
				return true;
			}
			String[] arr = StringUtils.split(dir, File.separator);
			StringBuffer sbfDir = new StringBuffer();
			// 循环生成子目录
			for (String s : arr) {
				sbfDir.append(File.separator);
				sbfDir.append(s);
				// 目录编码，解决中文路径问题
				d = new String(sbfDir.toString().getBytes(CHARSET_UTF8), CHARSET_DEFAULT);
				// 尝试切入目录
				if (client.changeWorkingDirectory(d)) {
					continue;
				}
				if (!client.makeDirectory(d)) {
					LOGGER.error("创建ftp目录失败:{}", sbfDir);
					return false;
				}
				LOGGER.info("创建ftp目录成功:{}", sbfDir);
			}
			// 将目录切换至指定路径
			return client.changeWorkingDirectory(d);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ftpClientPool.returnObject(client);
		}
	}
}
