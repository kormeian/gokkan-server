package com.gokkan.gokkan.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.baseUrl}")
	private String baseUrl;

	public String save(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new RestApiException(ImageErrorCode.EMPTY_FILE);
		}

		String fileName = createFileName(multipartFile.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			amazonS3.putObject(
				new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (IOException e) {
			throw new RestApiException(ImageErrorCode.INTERNAL_SERVER_ERROR);
		}

		return baseUrl + fileName;
	}

	public void check(MultipartFile multipartFiles) {
		String filename = multipartFiles.getOriginalFilename();
		if (filename != null) {
			checkName(filename);
		} else {
			throw new RestApiException(ImageErrorCode.MISMATCH_FILE_TYPE);
		}
	}

	public List<String> save(List<MultipartFile> multipartFiles) {
		if (multipartFiles.get(0).isEmpty()) {
			return new ArrayList<>();
		}

		List<String> urls = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFiles) {
			urls.add(save(multipartFile));
		}

		return urls;
	}

	public void check(List<MultipartFile> multipartFiles) {
		if (multipartFiles.get(0).isEmpty()) {
			return;
		}
		for (MultipartFile multipartFile : multipartFiles) {
			check(multipartFile);
		}
	}

	public void checkImageCount(List<MultipartFile> multipartFiles, int savedCount) {
		int multipartFiesSize = multipartFiles.size() == 1 ?
			(multipartFiles.get(0).isEmpty() ? -1 : 1)
			: multipartFiles.size();

		if (multipartFiesSize == -1) {
			return;
		}

		if (multipartFiesSize + savedCount > 5) {
			throw new RestApiException(ImageErrorCode.TOO_MANY_IMAGE);
		}
	}

	public boolean delete(String url) {
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucket, url.replaceAll(baseUrl, "")));
		} catch (Error e) {
			throw new RestApiException(ImageErrorCode.NOT_DELETED_IMAGE);
		}
		return true;
	}

	private void checkName(String fileName) {
		try {
			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg")) {
				return;
			}
			throw new RestApiException(ImageErrorCode.INVALID_FORMAT_FILE);
		} catch (StringIndexOutOfBoundsException e) {
			throw new RestApiException(ImageErrorCode.MISMATCH_FILE_TYPE);
		}
	}

	private String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	private String getFileExtension(String fileName) {
		try {
			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg")) {
				return extension;
			}
			throw new RestApiException(ImageErrorCode.INVALID_FORMAT_FILE);
		} catch (StringIndexOutOfBoundsException e) {
			throw new RestApiException(ImageErrorCode.MISMATCH_FILE_TYPE);
		}
	}
}