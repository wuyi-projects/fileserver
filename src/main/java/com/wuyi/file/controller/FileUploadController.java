/**
 * 
 * @date 2018年3月18日上午2:32:40
 * @version 1.0.0
 * @since 1.0.0 
 */
package com.wuyi.file.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wuyi.core.controller.BaseController;
import com.wuyi.file.constant.FileServerPathConst;
import com.wuyi.file.dto.FileInfoDTO;
import com.wuyi.file.model.FileInfo;
import com.wuyi.file.service.FileInfoService;
import com.wuyi.file.util.PathUtil;
import com.wuyi.file.util.file.FileType;
import com.wuyi.file.util.file.FileTypeUtil;
import com.wuyi.file.util.ftp.pool.client.FTPClientHelper;
import com.wuyi.util.Constants;
import com.wuyi.util.format.json.RequestJson;
import com.wuyi.util.format.json.StatusEnum;

/**
 * @author 张自强
 * @company 武汉东日科技有限公司
 *
 * @date 2018年3月18日上午2:32:40
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
@RequestMapping("/file")
public class FileUploadController extends BaseController {

	private static Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private FTPClientHelper ftpClientHelper;

	@Autowired
	private FileInfoService fileInfoService;

	@RequestMapping("/upload")
	@ResponseBody
	public Map<String, Object> upload(@RequestParam("file") MultipartFile uploadFile)
			throws Exception {

		try {

			String originalFilename = uploadFile.getOriginalFilename();
			FileInfo fileInfo = generatorPathAndFileName(FileServerPathConst.FILE_SERVER_ROOT_PATH, originalFilename);

			InputStream is = uploadFile.getInputStream();
			String sha256Str = DigestUtils.sha256Hex(is);
			fileInfo.setFingerprint(sha256Str);
			fileInfo.setSize(uploadFile.getSize());
			fileInfoService.save(fileInfo);

			uploadFile.transferTo(new File(FileServerPathConst.FILE_SERVER_ROOT_PATH + fileInfo.getPath()));

			return RequestJson.pack(StatusEnum.SUCCEESS, fileInfo.getUniqueId());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return RequestJson.pack(StatusEnum.FILE_UPLOAD_FAIL, null);
		}
	}

	@RequestMapping("/multiUpload")
	@ResponseBody
	public Map<String, Object> multiUpload(@RequestParam("files") MultipartFile[] uploadFiles) {
		try {
			List<String> list = new ArrayList<String>();
			List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
			FileInfo fileInfo = null;
			for (MultipartFile uploadFile : uploadFiles) {

				fileInfo = generatorPathAndFileName(FileServerPathConst.FILE_SERVER_ROOT_PATH, uploadFile.getOriginalFilename());
				list.add(fileInfo.getUniqueId());

				InputStream is = uploadFile.getInputStream();
				String sha256Str = DigestUtils.sha256Hex(is);
				fileInfo.setFingerprint(sha256Str);
				fileInfo.setSize(uploadFile.getSize());
				fileInfoList.add(fileInfo);

				uploadFile.transferTo(new File(FileServerPathConst.FILE_SERVER_ROOT_PATH + fileInfo.getPath()));
			}
			fileInfoService.batchSave(fileInfoList);
			return RequestJson.pack(StatusEnum.SUCCEESS, list);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return RequestJson.pack(StatusEnum.FILE_UPLOAD_FAIL, null);
		} catch (IOException e) {
			e.printStackTrace();
			return RequestJson.pack(StatusEnum.FILE_UPLOAD_FAIL, null);
		}
	}

	@RequestMapping("/uploadImage")
	@ResponseBody
	public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile uploadFile)
			throws Exception {

		try {
			FileType fileType = FileTypeUtil.getFileType(uploadFile.getInputStream());
			if (fileType == FileType.JPEG || fileType == FileType.PNG || fileType == FileType.BMP
					|| fileType == FileType.TIFF || fileType == FileType.GIF) {

				FileInfo fileInfo = generatorPathAndFileNameWithoutType(FileServerPathConst.IMAGE_SERVER_ROOT_PATH,
						uploadFile.getOriginalFilename());

				InputStream is = uploadFile.getInputStream();
				String sha256Str = DigestUtils.sha256Hex(is);
				fileInfo.setType(fileType.getExt());
				fileInfo.setFingerprint(sha256Str);
				fileInfo.setSize(uploadFile.getSize());

				uploadFile.transferTo(new File(FileServerPathConst.IMAGE_SERVER_ROOT_PATH + fileInfo.getPath()));

				fileInfoService.save(fileInfo);
				return RequestJson.pack(StatusEnum.SUCCEESS, fileInfo.getUniqueId());
			}
			return RequestJson.pack(StatusEnum.ILLEGAL_IMAGE_FILE_TYPE, "");
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return RequestJson.pack(StatusEnum.FILE_UPLOAD_FAIL, null);
		}
	}

	@RequestMapping("/multiUploadImage")
	@ResponseBody
	public Map<String, Object> multiUploadImage(@RequestParam("files") MultipartFile[] uploadFiles) {

		try {
			List<String> list = new ArrayList<String>();
			List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
			FileInfo fileInfo = null;
			for (MultipartFile uploadFile : uploadFiles) {
				FileType fileType = FileTypeUtil.getFileType(uploadFile.getInputStream());
				if (fileType == FileType.JPEG || fileType == FileType.PNG
						|| fileType == FileType.BMP || fileType == FileType.TIFF
						|| fileType == FileType.GIF) {

					fileInfo = generatorPathAndFileNameWithoutType(FileServerPathConst.IMAGE_SERVER_ROOT_PATH,
							uploadFile.getOriginalFilename());
					list.add(fileInfo.getUniqueId());

					InputStream is = uploadFile.getInputStream();
					String sha256Str = DigestUtils.sha256Hex(is);
					fileInfo.setType(fileType.getExt());
					fileInfo.setFingerprint(sha256Str);
					fileInfo.setSize(uploadFile.getSize());
					fileInfoList.add(fileInfo);

					uploadFile.transferTo(new File(FileServerPathConst.IMAGE_SERVER_ROOT_PATH + fileInfo.getPath()));
				}
			}
			fileInfoService.batchSave(fileInfoList);
			return RequestJson.pack(StatusEnum.SUCCEESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			return RequestJson.pack(StatusEnum.FILE_UPLOAD_FAIL, null);
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @date 2018年8月31日下午1:33:19
	 * @author SuperKoala
	 * @since 1.0.0
	 */
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(String uid) throws IOException {

		FileInfoDTO fileInfoDTO = fileInfoService.getByUniqueId(uid);

		File file = new File(FileServerPathConst.FILE_SERVER_ROOT_PATH, fileInfoDTO.getPath());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", fileInfoDTO.getUniqueId()
				+ Constants.SEPARATOR_DOT + fileInfoDTO.getType());
		HttpStatus statusCode = HttpStatus.OK;

		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, statusCode);
	}

	/**
	 * 显示图片
	 * 
	 * @param id
	 * @date 2018年8月31日上午10:10:57
	 * @author SuperKoala
	 * @throws IOException
	 * @since 1.0.0
	 */
	@RequestMapping(value = "/image")
	public void image(String uid) throws IOException {

		FileInfoDTO fileInfoDTO = fileInfoService.getByUniqueId(uid);

		File filePath = new File(FileServerPathConst.IMAGE_SERVER_ROOT_PATH + Constants.SEPARATOR_COMMON_DIRECTORY
				+ fileInfoDTO.getPath());
		if (filePath.exists()) {
			BufferedImage image = ImageIO.read(new FileInputStream(filePath));
			response.setContentType("image/" + fileInfoDTO.getType());
			response.setCharacterEncoding("UTF-8");
			if (image == null) {
				LOGGER.error("图片读取错误");
				throw new IOException();
			}
			if (image != null) {
				try {
					ImageIO.write(image, "JPEG", response.getOutputStream());
				} catch (IOException e) {
					LOGGER.error("ImageIO输出异常,{}", e);
					throw new IOException(e);
				}
			}
		}
	}

	@RequestMapping("/uploadImageForUM")
	@ResponseBody
	public Map<String, Object> uploadImageForCKEditor(
			@RequestParam("upfile") MultipartFile uploadFile) throws Exception {

		try {
			FileType fileType = FileTypeUtil.getFileType(uploadFile.getInputStream());
			if (fileType == FileType.JPEG || fileType == FileType.PNG || fileType == FileType.BMP
					|| fileType == FileType.TIFF || fileType == FileType.GIF) {

				FileInfo fileInfo = generatorPathAndFileNameWithoutType(FileServerPathConst.IMAGE_SERVER_ROOT_PATH,
						uploadFile.getOriginalFilename());

				InputStream is = uploadFile.getInputStream();
				String sha256Str = DigestUtils.sha256Hex(is);
				fileInfo.setType(fileType.getExt());
				fileInfo.setFingerprint(sha256Str);
				fileInfo.setSize(uploadFile.getSize());

				uploadFile.transferTo(new File(FileServerPathConst.IMAGE_SERVER_ROOT_PATH + fileInfo.getPath()));

				fileInfoService.save(fileInfo);

				Map<String, Object> result = new HashMap<String, Object>();
				result.put("state", "SUCCESS");
				result.put("url", "/wuyi_file/file/image?uid=" + fileInfo.getUniqueId());
				return result;
			}
			return RequestJson.pack(StatusEnum.ILLEGAL_IMAGE_FILE_TYPE, "");
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return RequestJson.pack(StatusEnum.FILE_UPLOAD_FAIL, null);
		}
	}

	/**
	 * 生成散列目录和UUID文件名
	 * 
	 * @param rootPath
	 *            根路径
	 * @param originalFilename
	 *            原始文件名
	 * @return 文件全路径
	 * @date 2018年8月31日下午1:34:34
	 * @author SuperKoala
	 * @since 1.0.0
	 */
	private FileInfo generatorPathAndFileName(String rootPath, String originalFilename) {

		String postfix = "";
		if (originalFilename.lastIndexOf(Constants.SEPARATOR_DOT) != -1) {
			postfix = originalFilename.substring(originalFilename
					.lastIndexOf(Constants.SEPARATOR_DOT) + 1);

		}
		String uniqueId = UUID.randomUUID().toString().replaceAll("\\-", "");
		String fullFileName = uniqueId + Constants.SEPARATOR_DOT + postfix;
		String path = PathUtil.generateDirectory(rootPath, fullFileName);

		StringBuilder sb = new StringBuilder();
		sb.append(path);
		sb.append(Constants.SEPARATOR_COMMON_DIRECTORY);
		sb.append(fullFileName);

		FileInfo fileInfo = new FileInfo();
		fileInfo.setUniqueId(uniqueId);
		fileInfo.setPath(sb.toString());
		fileInfo.setType(postfix);
		return fileInfo;
	}

	/**
	 * 生成散列目录和UUID文件名,返回信息不包含文件类型
	 * 
	 * @param rootPath
	 *            根路径
	 * @param originalFilename
	 *            原始文件名
	 * @return 文件全路径
	 * @date 2018年8月31日下午1:34:34
	 * @author SuperKoala
	 * @since 1.0.0
	 */
	private FileInfo generatorPathAndFileNameWithoutType(String rootPath, String originalFilename) {

		String postfix = "";
		if (originalFilename.lastIndexOf(Constants.SEPARATOR_DOT) != -1) {
			postfix = originalFilename.substring(originalFilename
					.lastIndexOf(Constants.SEPARATOR_DOT) + 1);

		}
		String uniqueId = UUID.randomUUID().toString().replaceAll("\\-", "");
		String fullFileName = uniqueId + Constants.SEPARATOR_DOT + postfix;
		String path = PathUtil.generateDirectory(rootPath, fullFileName);

		StringBuilder sb = new StringBuilder();
		sb.append(path);
		sb.append(Constants.SEPARATOR_COMMON_DIRECTORY);
		sb.append(fullFileName);

		FileInfo fileInfo = new FileInfo();
		fileInfo.setUniqueId(uniqueId);
		fileInfo.setPath(sb.toString());
		return fileInfo;
	}

}
