package com.gokkan.gokkan.domain.image.service;

import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.domain.image.repository.ImageItemRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageItemService {

	private final ImageItemRepository imageItemRepository;
	private final AwsS3Service awsS3Service;

	public List<ImageItem> create(List<String> urls) {
		if (urls.size() == 0) {
			throw new RestApiException(ImageErrorCode.EMPTY_URL);
		}

		List<ImageItem> imageItems = new ArrayList<>();
		for (String url : urls) {
			if (url == null || url.length() == 0) {
				throw new RestApiException(ImageErrorCode.INVALID_FORMAT_URL);
			}
			imageItems.add(
				ImageItem.builder()
					.url(url)
					.build()
			);
		}

		return imageItems;
	}

	public boolean delete(Long imageItemId) {
		ImageItem imageItem = getImageItem(imageItemId);
		String url = imageItem.getUrl();
		imageItemRepository.delete(imageItem);
		awsS3Service.delete(url);

		return true;
	}

	public boolean delete(ImageItem imageItem) {
		String url = imageItem.getUrl();
		imageItemRepository.delete(imageItem);
		awsS3Service.delete(url);

		return true;
	}

	private ImageItem getImageItem(Long imageItemId) {
		return imageItemRepository.findById(imageItemId)
			.orElseThrow(() -> new RestApiException(ImageErrorCode.NOT_FOUND_IMAGE_ITEM));
	}
}
