package com.gokkan.gokkan.domain.image.service;

import static com.gokkan.gokkan.domain.image.dto.ImageDto.CreateRequest;

import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.domain.image.exception.ImageException;
import com.gokkan.gokkan.domain.image.repository.ImageItemRepository;
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
	//TODO Item 생성 후
//	private final ItemRepository itemRepository;
	private final AwsS3Service awsS3Service;

	public List<ImageItem> save(CreateRequest request) {
		//TODO Item 생성 후
//		Item item = itemRepository.findById(request.getItemId())
//			.orElseThrow(new ImageException(ImageErrorCode.NOT_FOUND_IMAGE_ITEM));

		if (request.getUrls().size() == 0) {
			throw new ImageException(ImageErrorCode.EMPTY_URL);
		}

		List<ImageItem> imageItems = new ArrayList<>();
		for (String url : request.getUrls()) {
			if (url == null || url.length() == 0) {
				throw new ImageException(ImageErrorCode.INVALID_FORMAT_URL);
			}
			imageItems.add(imageItemRepository.save(
				ImageItem.builder()
					.url(url)
//					.item(item)
					.build()));
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

	//TODO Item 생성 후
//	public ImageItem update(UpdateRequest request) {
//		ImageItem imageItem = getImageItem(request.getImageId());
//		imageItem.setUrl(request.getUrl());
//		return imageItemRepository.save(imageItem);
//	}

	private ImageItem getImageItem(Long imageItemId) {
		return imageItemRepository.findById(imageItemId)
			.orElseThrow(() -> new ImageException(ImageErrorCode.NOT_FOUND_IMAGE_ITEM));
	}
}
