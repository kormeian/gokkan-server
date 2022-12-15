package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StyleItemService {

	private final StyleItemRepository styleItemRepository;
	private final StyleRepository styleRepository;

	@Transactional
	public StyleItem create(String name) {
		StyleItem styleItem = StyleItem.builder()
			.style(getStyle(name))
			.build();
		return styleItemRepository.save(styleItem);
	}

	@Transactional
	public StyleItem update(Long id, String name) {
		StyleItem styleItem = getStyleItem(id);
		styleItem.setStyle(getStyle(name));
		return styleItemRepository.save(styleItem);
	}

	private StyleItem getStyleItem(Long id) {
		return styleItemRepository.findById(id)
			.orElseThrow(() -> new RestApiException(StyleErrorCode.NOT_FOUND_STYLE_ITEM));
	}

	private Style getStyle(String name) {
		return styleRepository.findByName(name)
			.orElseThrow(() -> new RestApiException(StyleErrorCode.NOT_FOUND_STYLE));
	}
}
