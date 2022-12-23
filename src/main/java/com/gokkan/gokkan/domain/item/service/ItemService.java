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
import java.util.ArrayList;
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

	@Transactional
	public Response create(
		UpdateRequest request,
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		Member member) {
		Item item = updateItem(request, imageItemFiles, imageCheckFiles, member);
		item.setState(State.ASSESSING);
		return Response.toResponse(itemRepository.save(item));
	}

	@Transactional(readOnly = true)
	public Response readDetail(Long itemId) {
		Item item = getItem(itemId);
		itemStateCheckForRead(item.getState(),
			new ArrayList<>(List.of(State.COMPLETE, State.ASSESSING)));
		return Response.toResponse(item);
	}

	@Transactional(readOnly = true)
	public Response readTempDetail(Long itemId, Member member) {
		Item item = getItem(itemId);
		memberLoginCheck(member);
		memberMatchCheck(member.getUserId(), item.getMember().getUserId());
		itemStateCheckForRead(item.getState(),
			new ArrayList<>(List.of(State.TEMPORARY, State.RETURN)));
		return Response.toResponse(item);
	}

	@Transactional
	public boolean delete(Long itemId, Member member) {
		memberLoginCheck(member);
		Item item = getItem(itemId);
		memberMatchCheck(member.getUserId(), item.getMember().getUserId());
		itemStateCheckForUpdateAndDelete(item.getState());
		imageItemService.deleteAllImageItems(item.getImageItems());
		imageCheckService.deleteAllImageItems(item.getImageChecks());
		itemRepository.delete(item);
		return true;
	}

	@Transactional
	public Response update(
		UpdateRequest request,
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		Member member) {
		return Response.toResponse(
			itemRepository.save(updateItem(request, imageItemFiles, imageCheckFiles, member)));
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

	private Item updateItem(UpdateRequest request, List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles, Member member) {
		memberLoginCheck(member);
		Item item = getItem(request.getItemId());
		memberMatchCheck(member.getUserId(), item.getMember().getUserId());
		itemStateCheckForUpdateAndDelete(item.getState());

		checkImageFiles(imageItemFiles, imageCheckFiles,
			request.getImageItemUrls().size(), request.getImageCheckUrls().size());

		Category category = categoryService.getCategory(request.getCategory());
		List<StyleItem> styleItems = styleItemService.createNotDuplicate(request.getStyles(),
			item.getStyleItems());
		List<ImageItem> imageItems = imageItemService.create(awsS3Service.save(imageItemFiles));
		List<ImageCheck> imageChecks = imageCheckService.create(awsS3Service.save(imageCheckFiles));

		item = request.toItem(item, category);
		item.setImageItems(
			imageItemService.checkImageItemDeleted(
				request.getImageItemUrls(),
				item.getImageItems()));
		item.setImageChecks(
			imageCheckService.checkImageCheckDeleted(
				request.getImageCheckUrls(),
				item.getImageChecks()));

		saveItemRelations(imageItems, imageChecks, styleItems, item);

		item.setThumbnail(
			item.getImageItems().size() == 0 ? "" : item.getImageItems().get(0).getUrl());
		return item;
	}

	private void checkImageFiles(
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		int imageItemsUrlsCount,
		int imageCheckUrlsCount) {
		awsS3Service.checkImageCount(imageItemFiles, imageItemsUrlsCount);
		awsS3Service.checkImageCount(imageCheckFiles, imageCheckUrlsCount);
		awsS3Service.check(imageItemFiles);
		awsS3Service.check(imageCheckFiles);
	}

	private static void memberMatchCheck(String memberId, String itemMemberId) {
		if (!memberId.equals(itemMemberId)) {
			throw new RestApiException(MemberErrorCode.MEMBER_MISMATCH);
		}
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

		styleItemRepository.saveAll(styleItems);
		imageItemRepository.saveAll(imageItems);
		imageCheckRepository.saveAll(imageChecks);
	}

	private static void memberLoginCheck(Member member) {
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_LOGIN);
		}
	}

	private static void itemStateCheckForUpdateAndDelete(State state) {
		if (state == State.COMPLETE || state == State.ASSESSING) {
			throw new RestApiException(ItemErrorCode.CAN_NOT_FIX_STATE);
		}
	}

	private static void itemStateCheckForRead(State itemState, List<State> states) {
		for (State state : states) {
			if (state == itemState) {
				return;
			}
		}
		throw new RestApiException(ItemErrorCode.CAN_NOT_READ_STATE);
	}
}
