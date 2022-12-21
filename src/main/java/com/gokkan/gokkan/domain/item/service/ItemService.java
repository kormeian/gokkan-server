package com.gokkan.gokkan.domain.item.service;

import static com.gokkan.gokkan.domain.item.dto.ItemDto.Response;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.service.CategoryService;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import com.gokkan.gokkan.domain.image.repository.ImageItemRepository;
import com.gokkan.gokkan.domain.image.service.AwsS3Service;
import com.gokkan.gokkan.domain.image.service.ImageCheckService;
import com.gokkan.gokkan.domain.image.service.ImageItemService;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.dto.ItemDto.CreateRequest;
import com.gokkan.gokkan.domain.item.dto.ItemDto.UpdateRequest;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.service.StyleItemService;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final StyleItemRepository styleItemRepository;
	private final ImageItemRepository imageItemRepository;
	private final ImageCheckRepository imageCheckRepository;

	private final CategoryService categoryService;
	private final StyleItemService styleItemService;
	private final ImageItemService imageItemService;
	private final ImageCheckService imageCheckService;
	private final AwsS3Service awsS3Service;

	private static void memberMatchCheck(String memberId, String itemMemberId) {
		if (!memberId.equals(itemMemberId)) {
			throw new RestApiException(MemberErrorCode.MEMBER_MISMATCH);
		}
	}

	private static void memberLoginCheck(Member member) {
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_LOGIN);
		}
	}

	@Transactional
	public Response create(
		CreateRequest request,
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		Member member) {
		memberLoginCheck(member);

		checkImageFiles(imageItemFiles, imageCheckFiles);

		Category category = categoryService.getCategory(request.getCategory());
		List<StyleItem> styleItems = styleItemService.create(request.getStyles());
		List<ImageItem> imageItems = imageItemService.create(awsS3Service.save(imageItemFiles));
		List<ImageCheck> imageChecks = imageCheckService.create(awsS3Service.save(imageCheckFiles));

		Item item = itemRepository.save(request.toItem(category, member));
		saveItemRelations(imageItems, imageChecks, styleItems, item);
		return Response.toResponse(itemRepository.save(item));
	}

	@Transactional(readOnly = true)
	public Response read(Long itemId) {
		Item item = getItem(itemId);
		return Response.toResponse(item);
	}

	@Transactional
	public boolean delete(Long itemId, Member member) {
		memberLoginCheck(member);
		Item item = getItem(itemId);
		memberMatchCheck(member.getUserId(), item.getMember().getUserId());

		deleteImageItems(item.getImageItems());
		deleteImageChecks(item.getImageChecks());
		itemRepository.delete(item);
		return true;
	}

	@Transactional
	public Response update(
		UpdateRequest request,
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		Member member) {

		memberLoginCheck(member);
		Item item = getItem(request.getItemId());
		memberMatchCheck(member.getUserId(), item.getMember().getUserId());

		checkImageFiles(imageItemFiles, imageCheckFiles);

		Category category = categoryService.getCategory(request.getCategory());
		List<StyleItem> styleItems = styleItemService.create(request.getStyles());
		List<ImageItem> imageItems = imageItemService.create(awsS3Service.save(imageItemFiles));
		List<ImageCheck> imageChecks = imageCheckService.create(awsS3Service.save(imageCheckFiles));

		item = request.toItem(item, category);

		deleteStyleItems(item.getStyleItems());
		deleteImageItems(item.getImageItems());
		deleteImageChecks(item.getImageChecks());

		saveItemRelations(imageItems, imageChecks, styleItems, item);

		return Response.toResponse(itemRepository.save(item));
	}


	public Long createTemporary(Member member) {
		memberLoginCheck(member);
		return itemRepository.save(
				Item.builder()
					.member(member)
					.state(State.TEMPORARY)
					.created(LocalDateTime.now())
					.updated(LocalDateTime.now())
					.build())
			.getId();
	}

	private void deleteImageChecks(List<ImageCheck> imageChecksSaved) {
		if (imageChecksSaved != null && imageChecksSaved.size() != 0) {
			for (ImageCheck imageCheck : imageChecksSaved) {
				imageCheckService.delete(imageCheck);
			}
		}
	}

	private void deleteImageItems(List<ImageItem> imageItemsSaved) {
		if (imageItemsSaved != null && imageItemsSaved.size() != 0) {
			for (ImageItem imageItem : imageItemsSaved) {
				imageItemService.delete(imageItem);
			}
		}
	}

	private void deleteStyleItems(List<StyleItem> styleItemsSaved) {
		if (styleItemsSaved != null && styleItemsSaved.size() != 0) {
			styleItemRepository.deleteAll(styleItemsSaved);
		}
	}

	private void checkImageFiles(List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles) {
		awsS3Service.check(imageItemFiles);
		awsS3Service.check(imageCheckFiles);
	}

	private Item getItem(Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow((() -> new RestApiException(ItemErrorCode.NOT_FOUND_ITEM)));
	}

	private void saveItemRelations(
		List<ImageItem> imageItems,
		List<ImageCheck> imageChecks,
		List<StyleItem> styleItems,
		Item item) {

		item.addStyleItem(styleItems);
		item.addImageItems(imageItems);
		item.addImageChecks(imageChecks);

		imageItemRepository.saveAll(imageItems);
		imageCheckRepository.saveAll(imageChecks);
		styleItemRepository.saveAll(styleItems);
	}
}
