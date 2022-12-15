package com.gokkan.gokkan.domain.image.service;

import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageCheckService {

	private final ImageCheckRepository imageCheckRepository;
	private final ItemRepository itemRepository;
	private final AwsS3Service awsS3Service;


	public List<ImageCheck> save(List<String> urls) {
		if (urls.size() == 0) {
			throw new RestApiException(ImageErrorCode.EMPTY_URL);
		}

		List<ImageCheck> imageChecks = new ArrayList<>();
		for (String url : urls) {
			if (url == null || url.length() == 0) {
				throw new RestApiException(ImageErrorCode.INVALID_FORMAT_URL);
			}

			imageChecks.add(imageCheckRepository.save(
				ImageCheck.builder()
					.url(url)
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

	public boolean delete(ImageCheck imageCheck) {
		String url = imageCheck.getUrl();
		imageCheckRepository.delete(imageCheck);
		awsS3Service.delete(url);

		return true;
	}


	private ImageCheck getImageCheck(Long imageCheckId) {
		return imageCheckRepository.findById(imageCheckId)
			.orElseThrow(() -> new RestApiException(ImageErrorCode.NOT_FOUND_IMAGE_CHECK));
	}

}
