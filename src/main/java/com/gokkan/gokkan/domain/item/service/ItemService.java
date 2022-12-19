package com.gokkan.gokkan.domain.item.service;

import static com.gokkan.gokkan.domain.item.dto.ItemDto.Response;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.exception.CategoryErrorCode;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.service.ImageCheckService;
import com.gokkan.gokkan.domain.image.service.ImageItemService;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.dto.ItemDto.CreateRequest;
import com.gokkan.gokkan.domain.item.dto.ItemDto.UpdateRequest;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final StyleItemRepository styleItemRepository;
	private final ImageItemService imageItemService;
	private final ImageCheckService imageCheckService;


	@Transactional
	public Response create(
		CreateRequest request,
		List<ImageItem> imageItemUrls,
		List<ImageCheck> imageCheckUrls,
		Category category,
		List<StyleItem> styleItems) {

		Item item = request.toItem();
		item.setCategory(category);
		item.addStyleItem(styleItems);
		item.addImageItems(imageItemUrls);
		item.addImageChecks(imageCheckUrls);

		return Response.toResponse(itemRepository.save(item));
	}

	@Transactional(readOnly = true)
	public Response read(Long itemId) {
		Item item = getItem(itemId);
		return Response.toResponse(item);
	}

	@Transactional
	public boolean delete(Long itemId) {
		Item item = getItem(itemId);
		List<ImageItem> imageItems = item.getImageItems();
		imageItems.stream().map(imageItem -> imageItemService.delete(imageItem.getId()));

		List<ImageCheck> imageChecks = item.getImageChecks();
		imageChecks.stream().map(imageItem -> imageCheckService.delete(imageItem.getId()));
		itemRepository.delete(item);

		return true;
	}

	@Transactional
	public Response update(
		UpdateRequest request,
		List<ImageItem> imageItems,
		List<ImageCheck> imageChecks,
		Category category,
		List<StyleItem> styleItems) {

		Item item = getItem(request.getItemId());
		item = request.toItem(item);

		item.setCategory(category);

		List<StyleItem> styleItemsSaved = item.getStyleItems();
		if (styleItemsSaved != null && styleItemsSaved.size() != 0) {
			styleItemRepository.deleteAll(styleItemsSaved);
		}
		item.addStyleItem(styleItems);

		List<ImageItem> imageItemsSaved = item.getImageItems();
		if (imageItemsSaved != null && imageItemsSaved.size() != 0) {
			for (ImageItem imageItem : imageItemsSaved) {
				imageItemService.delete(imageItem);
			}
		}

		List<ImageCheck> imageChecksSaved = item.getImageChecks();
		if (imageChecksSaved != null && imageChecksSaved.size() != 0) {
			for (ImageCheck imageCheck : imageChecksSaved) {
				imageCheckService.delete(imageCheck);
			}
		}

		item.addImageItems(imageItems);
		item.addImageChecks(imageChecks);

		return Response.toResponse(itemRepository.save(item));
	}

	private Item getItem(Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow((() -> new RestApiException(ItemErrorCode.NOT_FOUND_ITEM)));
	}
}
