package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StyleItemService {

	private final StyleItemRepository styleItemRepository;
	private final StyleRepository styleRepository;

	public List<StyleItem> createNotDuplicate(List<String> names, List<StyleItem> saved) {
		for (String name : names) {
			if (!styleRepository.existsByName(name)) {
				throw new RestApiException(StyleErrorCode.NOT_FOUND_STYLE);
			}
		}

		boolean[] deleted = new boolean[saved.size()];        // true -> 삭제 해야할 것 			false -> 삭제 안하고 저장 해 놓을것
		boolean[] duplicate = new boolean[names.size()];    // true -> 저장 된 것과 중복된 것 	false -> 중복 안되어서 StyleItem 생성 할 것

		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			for (int j = 0; j < saved.size(); j++) {
				if (name.equals(saved.get(j).getStyle().getName())) {
					duplicate[i] = true;
					deleted[j] = true;
					break;
				}
			}
		}

		List<StyleItem> styleItems = new ArrayList<>();
		for (int i = 0; i < deleted.length; i++) {
			if (deleted[i]) {
				styleItems.add(saved.get(i));
			}
		}

		for (int i = 0; i < duplicate.length; i++) {
			if (!duplicate[i]) {
				styleItems.add(StyleItem.builder()
					.style(getStyle(names.get(i)))
					.build());
			}
		}

		return styleItems;
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

	public void deleteAll(List<StyleItem> styleItems) {
		styleItemRepository.deleteAll(styleItems);
	}
}
