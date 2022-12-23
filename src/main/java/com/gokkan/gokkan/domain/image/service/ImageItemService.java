package com.gokkan.gokkan.domain.image.service;

import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.dto.ImageDto.UpdateRequest;
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
		List<ImageItem> imageItems = new ArrayList<>();
		for (String url : urls) {
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

	public void deleteAllImageItems(List<ImageItem> saved) {
		for (ImageItem imageItem : saved) {
			delete(imageItem);
		}
	}

	private ImageItem getImageItem(Long imageItemId) {
		return imageItemRepository.findById(imageItemId)
			.orElseThrow(() -> new RestApiException(ImageErrorCode.NOT_FOUND_IMAGE_ITEM));
	}

	public List<ImageItem> checkImageItemDeleted(List<UpdateRequest> urls, List<ImageItem> saved) {
		List<ImageItem> imageItems = new ArrayList<>();
		boolean[] deleted = new boolean[saved.size()];
		int deletedCount = saved.size() - urls.size();

		if (deletedCount == 0) {
			return saved;
		} else if (deletedCount == saved.size()) {
			deleteAllImageItems(saved);
			return new ArrayList<>();
		} else {
			int nextStartIndex = 0;

			for (ImageItem imageItem : saved) {
				for (int j = nextStartIndex; j < urls.size(); j++) {
					if (imageItem.getId().equals(urls.get(j).getImageId())) {
						deleted[j] = true;
						nextStartIndex = j;
						break;
					}

				}
			}

			for (int i = 0; i < deleted.length; i++) {
				if (deleted[i]) {
					imageItems.add(saved.get(i));
				} else {
					delete(saved.get(i));
				}
			}
			return imageItems;
		}
	}
}
