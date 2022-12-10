package com.gokkan.gokkan.domain.image.service;

import static com.gokkan.gokkan.domain.image.dto.ImageDto.CreateRequest;
import static com.gokkan.gokkan.domain.image.dto.ImageDto.UpdateRequest;

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


	public List<ImageItem> save(CreateRequest request) {
		List<ImageItem> imageItems = new ArrayList<>();
		for (String url : request.getUrls()) {
			imageItems.add(imageItemRepository.save(
				ImageItem.builder()
					.url(url)
//					.item(request.getItemId())
					.build()));
		}

		return imageItems;
	}

	public boolean delete(Long imageItemId) {
		imageItemRepository.delete(getImageItem(imageItemId));
		return true;
	}

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
