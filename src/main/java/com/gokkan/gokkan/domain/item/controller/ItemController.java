package com.gokkan.gokkan.domain.item.controller;

import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.service.AwsS3Service;
import com.gokkan.gokkan.domain.image.service.ImageCheckService;
import com.gokkan.gokkan.domain.image.service.ImageItemService;
import com.gokkan.gokkan.domain.item.dto.ItemDto;
import com.gokkan.gokkan.domain.item.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

	private final ItemService itemService;
	private final ImageItemService imageItemService;
	private final ImageCheckService imageCheckService;
	private final AwsS3Service awsS3Service;

	@PostMapping("")
	public ResponseEntity<?> create(
		@RequestBody ItemDto.CreateRequest request,
		@RequestPart List<MultipartFile> imageItemFiles,
		@RequestPart List<MultipartFile> imageCheckFiles) {

		checkBeforeSaveInS3(request.getCategory(), imageItemFiles, imageCheckFiles);

		List<ImageItem> imageItems = imageItemService.save(awsS3Service.save(imageItemFiles));
		List<ImageCheck> imageChecks = imageCheckService.save(awsS3Service.save(imageCheckFiles));

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(itemService.create(request, imageItems, imageChecks));
	}

	@GetMapping("")
	public ResponseEntity<?> read(@RequestParam Long itemId) {
		return ResponseEntity.ok(itemService.read(itemId));
	}

	@DeleteMapping
	public ResponseEntity<?> delete(@RequestParam Long itemId) {
		return ResponseEntity.ok(itemService.delete(itemId));
	}

	@PutMapping("")
	public ResponseEntity<?> update(
		@RequestBody ItemDto.UpdateRequest request,
		@RequestPart List<MultipartFile> imageItemFiles,
		@RequestPart List<MultipartFile> imageCheckFiles) {

		checkBeforeSaveInS3(request.getCategory(), imageItemFiles, imageCheckFiles);

		List<ImageItem> imageItems = imageItemService.save(awsS3Service.save(imageItemFiles));
		List<ImageCheck> imageChecks = imageCheckService.save(awsS3Service.save(imageCheckFiles));

		return ResponseEntity.ok(itemService.update(request, imageItems, imageChecks));
	}

	private void checkBeforeSaveInS3(String request, List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles) {
		itemService.checked(request);
		awsS3Service.check(imageItemFiles);
		awsS3Service.check(imageCheckFiles);
	}
}
