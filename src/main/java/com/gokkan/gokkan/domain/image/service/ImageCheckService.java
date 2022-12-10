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


	public List<ImageCheck> save(CreateRequest request) {
		List<ImageCheck> imageChecks = new ArrayList<>();
		for (String url : request.getUrls()) {
			imageChecks.add(imageCheckRepository.save(
				ImageCheck.builder()
					.url(url)
//					.item(request.getItemId())
					.build()));
		}

		return imageChecks;
	}

	public boolean delete(Long imageCheckId) {
		imageCheckRepository.delete(getImageCheck(imageCheckId));
		return true;
	}

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
