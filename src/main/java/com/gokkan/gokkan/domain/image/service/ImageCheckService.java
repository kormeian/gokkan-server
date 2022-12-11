package com.gokkan.gokkan.domain.image.service;

import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.dto.ImageDto.CreateRequest;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.domain.image.exception.ImageException;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageCheckService {

	private final ImageCheckRepository imageCheckRepository;
	//TODO Item 생성 후
//	private final ItemRepository itemRepository;
	private final AwsS3Service awsS3Service;


	public List<ImageCheck> save(CreateRequest request) {
		//TODO Item 생성 후
//		Item item = itemRepository.findById(request.getItemId())
//			.orElseThrow(new ImageException(ImageErrorCode.NOT_FOUND_IMAGE_ITEM));

		if (request.getUrls().size() == 0) {
			throw new ImageException(ImageErrorCode.EMPTY_URL);
		}

		List<ImageCheck> imageChecks = new ArrayList<>();
		for (String url : request.getUrls()) {
			if (url == null || url.length() == 0) {
				throw new ImageException(ImageErrorCode.INVALID_FORMAT_URL);
			}
			imageChecks.add(imageCheckRepository.save(
				ImageCheck.builder()
					.url(url)
//					.item(item)
					.build()));
		}

		return imageChecks;
	}

	public boolean delete(Long imageCheckId) {
		ImageCheck imageCheck = getImageCheck(imageCheckId);
		String url = imageCheck.getUrl();
		imageCheckRepository.delete(imageCheck);
		awsS3Service.delete(url);

		return true;
	}

	//TODO Item 생성 후
//	public ImageCheck update(UpdateRequest request) {
//		ImageCheck imageCheck = getImageCheck(request.getImageId());
//		imageCheck.setUrl(request.getUrl());
//		return imageCheckRepository.save(imageCheck);
//	}

	private ImageCheck getImageCheck(Long imageCheckId) {
		return imageCheckRepository.findById(imageCheckId)
				.orElseThrow(() -> new ImageException(ImageErrorCode.NOT_FOUND_IMAGE_CHECK));
	}

}
