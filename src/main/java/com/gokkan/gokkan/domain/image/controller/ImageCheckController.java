package com.gokkan.gokkan.domain.image.controller;

import com.gokkan.gokkan.domain.image.dto.ImageDto;
import com.gokkan.gokkan.domain.image.service.AwsS3Service;
import com.gokkan.gokkan.domain.image.service.ImageCheckService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("ImageChecks")
public class ImageCheckController {

	private final ImageCheckService imageCheckService;

	private final AwsS3Service awsS3Service;

	@PostMapping("")
	public ResponseEntity<?> save(
		@RequestPart List<MultipartFile> multipartFiles,
		@RequestParam Long itemId) {
		return ResponseEntity.ok(
			imageCheckService.save(
				ImageDto.CreateRequest.builder()
					.itemId(itemId)
					.urls(awsS3Service.save(multipartFiles))
					.build()
			)
		);
	}

	@DeleteMapping("")
	public ResponseEntity<?> delete(@RequestParam Long imageCheckId) {
		return ResponseEntity.ok(imageCheckService.delete(imageCheckId));
	}
}
