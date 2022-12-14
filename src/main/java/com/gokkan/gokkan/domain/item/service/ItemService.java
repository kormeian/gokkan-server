package com.gokkan.gokkan.domain.item.service;

import static com.gokkan.gokkan.domain.item.dto.ItemDto.Response;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.exception.CategoryErrorCode;
import com.gokkan.gokkan.domain.category.exception.CategoryException;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.service.ImageCheckService;
import com.gokkan.gokkan.domain.image.service.ImageItemService;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.dto.ItemDto.CreateRequest;
import com.gokkan.gokkan.domain.item.dto.ItemDto.UpdateRequest;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.exception.ItemException;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final ImageItemService imageItemService;
	private final ImageCheckService imageCheckService;


	@Transactional
	public Response create(
		CreateRequest request,
		List<ImageItem> imageItemUrls,
		List<ImageCheck> imageCheckUrls) {

		Item item = request.toItem();
		item.setCategory(getCategory(request.getCategory()));
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
		List<ImageCheck> imageChecks) {

		Item item = getItem(request.getItemId());
		item = request.toItem(item);
		item.setCategory(getCategory(request.getCategory()));

		for (ImageItem imageItem : item.getImageItems()) {
			imageItemService.delete(imageItem);
		}

		for (ImageCheck imageCheck : item.getImageChecks()) {
			imageCheckService.delete(imageCheck);
		}

		item.addImageItems(imageItems);
		item.addImageChecks(imageChecks);

		return Response.toResponse(itemRepository.save(item));
	}

	public void checked(String categoryName) {
		if (!categoryRepository.existsByName(categoryName)) {
			throw new CategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY);
		}
	}

	private Item getItem(Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow((() -> new ItemException(ItemErrorCode.NOT_FOUND_ITEM)));
	}

	private Category getCategory(String categoryName) {
		return categoryRepository.findByName(categoryName).orElseThrow(
			() -> new CategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY));
	}
}
