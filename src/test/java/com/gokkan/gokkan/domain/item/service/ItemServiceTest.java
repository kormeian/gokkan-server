package com.gokkan.gokkan.domain.item.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.service.ImageCheckService;
import com.gokkan.gokkan.domain.image.service.ImageItemService;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.dto.ItemDto;
import com.gokkan.gokkan.domain.item.dto.ItemDto.CreateRequest;
import com.gokkan.gokkan.domain.item.dto.ItemDto.Response;
import com.gokkan.gokkan.domain.item.dto.ItemDto.UpdateRequest;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	static Category root = Category.builder()
		.id(0L)
		.parent(null)
		.level(0)
		.name("root")
		.children(new ArrayList<>())
		.build();
	List<ImageItem> imageItems = List.of(getImageItem("item1"));
	List<ImageCheck> imageChecks = List.of(getImageCheck("check1"), getImageCheck("check2"));
	ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
	@Mock
	private ItemRepository itemRepository;
	@Mock
	private ImageItemService imageItemService;
	@Mock
	private ImageCheckService imageCheckService;
	@InjectMocks
	private ItemService itemService;

	private static CreateRequest getCreateRequest() {
		return ItemDto.CreateRequest.builder()
			.name("test name")
			.category("test category")
			.styles(List.of("st1", "st2"))
			.startPrice(100)
			.length(100L)
			.width(100L)
			.depth(100L)
			.height(100L)
			.material("나무")
			.conditionGrade("test conditionGrade")
			.conditionDescription("test conditionDescription")
			.text("test text")
			.madeIn("test madeIn")
			.designer("test designer")
			.brand("test brand")
			.productionYear(2023)
			.build();
	}

	private static UpdateRequest getUpdateRequest() {
		return ItemDto.UpdateRequest.builder()
			.itemId(1L)
			.name("update name")
			.category("update category")
			.styles(List.of("st3", "st4"))
			.startPrice(200)
			.length(200L)
			.width(200L)
			.depth(200L)
			.height(200L)
			.material("철제")
			.conditionGrade("update conditionGrade")
			.conditionDescription("update conditionDescription")
			.text("update text")
			.madeIn("update madeIn")
			.designer("update designer")
			.brand("update brand")
			.productionYear(1023)
			.build();
	}

	private static Item getItem(List<ImageItem> imageItems, List<ImageCheck> imageChecks) {
		return Item.builder()
			.name("test name")
			.category(getCategory("test category", root))
			.startPrice(100)
			.length(100L)
			.width(100L)
			.depth(100L)
			.height(100L)
			.material("나무")
			.conditionGrade("test conditionGrade")
			.conditionDescription("test conditionDescription")
			.text("test text")
			.madeIn("test madeIn")
			.designer("test designer")
			.brand("test brand")
			.productionYear(2023)
			.state(State.ASSESSING)
			.assessed(false)
			.created(LocalDateTime.now())
			.updated(LocalDateTime.now())
			.imageItems(new ArrayList<>(imageItems))
			.imageChecks(new ArrayList<>(imageChecks))
			.build();
	}

	private static ImageItem getImageItem(String url) {
		return ImageItem.builder()
			.url(url)
			.build();
	}

	private static ImageCheck getImageCheck(String url) {
		return ImageCheck.builder()
			.url(url)
			.build();
	}

	private static Category getCategory(String name, Category parent) {
		return Category.builder()
			.name(name)
			.parent(parent)
			.children(new ArrayList<>())
			.level(parent.getLevel() + 1)
			.build();
	}

	@DisplayName("01_00. create success")
	@Test
	public void test_01_00() {
		//given
		given(itemRepository.save(any())).willReturn(getItem(imageItems, imageChecks));

		//when
		CreateRequest createRequest = getCreateRequest();
		itemService.create(
			createRequest,
			getCategory("test category", root),
			getStyleItems(),
			imageItems,
			imageChecks);

		verify(itemRepository, times(1)).save(itemCaptor.capture());

		//then
		Item item = itemCaptor.getValue();

		assertEquals(item.getName(), createRequest.getName());
		assertEquals(item.getStartPrice(), createRequest.getStartPrice());
		assertEquals(item.getState(), State.ASSESSING);
		assertEquals(item.getCategory().getName(), createRequest.getCategory());
		assertEquals(item.getStyleItems().get(0).getStyle().getName(),
			createRequest.getStyles().get(0));
		assertEquals(item.getStyleItems().get(0).getItem().getName(), createRequest.getName());
		assertEquals(item.getStyleItems().get(1).getStyle().getName(),
			createRequest.getStyles().get(1));
		assertEquals(item.getStyleItems().get(1).getItem().getName(), createRequest.getName());

		assertEquals(item.getLength(), createRequest.getLength());
		assertEquals(item.getWidth(), createRequest.getWidth());
		assertEquals(item.getDepth(), createRequest.getDepth());
		assertEquals(item.getHeight(), createRequest.getHeight());
		assertEquals(item.getMaterial(), createRequest.getMaterial());
		assertEquals(item.getConditionGrade(), createRequest.getConditionGrade());
		assertEquals(item.getConditionDescription(), createRequest.getConditionDescription());
		assertEquals(item.getText(), createRequest.getText());
		assertEquals(item.getMadeIn(), createRequest.getMadeIn());
		assertEquals(item.getDesigner(), createRequest.getDesigner());
		assertEquals(item.getBrand(), createRequest.getBrand());
		assertEquals(item.getProductionYear(), createRequest.getProductionYear());
		assertFalse(item.isAssessed());

		assertEquals(item.getImageChecks().size(), 2);
		assertEquals(item.getImageChecks().get(0).getUrl(), imageChecks.get(0).getUrl());
		assertEquals(item.getImageChecks().get(1).getUrl(), imageChecks.get(1).getUrl());

		assertEquals(item.getImageItems().size(), 1);
		assertEquals(item.getImageItems().get(0).getUrl(), imageItems.get(0).getUrl());
	}

	@DisplayName("02_00. read success")
	@Test
	public void test_02_00() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(getItem(new ArrayList<>(), imageChecks)));

		//when
		CreateRequest savedItem = getCreateRequest();
		Response response = itemService.read(1L);

		//then
		assertEquals(response.getLength(), savedItem.getLength());
		assertEquals(response.getWidth(), savedItem.getWidth());
		assertEquals(response.getDepth(), savedItem.getDepth());
		assertEquals(response.getHeight(), savedItem.getHeight());
		assertEquals(response.getMaterial(), savedItem.getMaterial());
		assertEquals(response.getConditionGrade(), savedItem.getConditionGrade());
		assertEquals(response.getConditionDescription(), savedItem.getConditionDescription());
		assertEquals(response.getText(), savedItem.getText());
		assertEquals(response.getMadeIn(), savedItem.getMadeIn());
		assertEquals(response.getDesigner(), savedItem.getDesigner());
		assertEquals(response.getBrand(), savedItem.getBrand());
		assertEquals(response.getProductionYear(), savedItem.getProductionYear());
		assertFalse(response.isAssessed());

		assertEquals(response.getImageCheckUrls().size(), 2);
		assertEquals(response.getImageCheckUrls().get(0), imageChecks.get(0).getUrl());
		assertEquals(response.getImageCheckUrls().get(1), imageChecks.get(1).getUrl());

		assertEquals(response.getImageItemUrls().size(), 0);
	}

	@DisplayName("02_01. read fail not found Item")
	@Test
	public void test_02_01() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.empty());

		//when
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.read(1L));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	@DisplayName("03_00. delete success")
	@Test
	public void test_03_00() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(getItem(imageItems, imageChecks)));

		//when
		boolean deleted = itemService.delete(1L);
		verify(itemRepository, times(1)).delete(itemCaptor.capture());

		//then
		assertTrue(deleted);
	}

	@DisplayName("03_01. delete fail not found item")
	@Test
	public void test_03_01() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.empty());

		//when
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.delete(1L));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	@DisplayName("04_00. update success")
	@Test
	public void test_04_00() {
		//given
		Item save = getItem(imageItems, imageChecks);
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(save));
		given(itemRepository.save(any())).willReturn(save);

		imageItems = List.of(getImageItem("update imageItem1"), getImageItem("update imageItem2"));
		imageChecks = List.of((getImageCheck("update imageCheck1")));

		//when
		UpdateRequest updateRequest = getUpdateRequest();
		itemService.update(
			updateRequest,
			getCategory("update category", root),
			getStyleItems(),
			imageItems,
			imageChecks);
		verify(itemRepository, times(1)).save(itemCaptor.capture());
		verify(imageItemService, times(1)).delete(any(ImageItem.class));
		verify(imageCheckService, times(2)).delete(any(ImageCheck.class));

		//then
		Item item = itemCaptor.getValue();

		assertEquals(item.getName(), updateRequest.getName());
		assertEquals(item.getStartPrice(), updateRequest.getStartPrice());
		assertEquals(item.getState(), State.ASSESSING);
		assertEquals(item.getCategory().getName(), updateRequest.getCategory());

		assertEquals(item.getLength(), updateRequest.getLength());
		assertEquals(item.getWidth(), updateRequest.getWidth());
		assertEquals(item.getDepth(), updateRequest.getDepth());
		assertEquals(item.getHeight(), updateRequest.getHeight());
		assertEquals(item.getMaterial(), updateRequest.getMaterial());
		assertEquals(item.getConditionGrade(), updateRequest.getConditionGrade());
		assertEquals(item.getConditionDescription(), updateRequest.getConditionDescription());
		assertEquals(item.getText(), updateRequest.getText());
		assertEquals(item.getMadeIn(), updateRequest.getMadeIn());
		assertEquals(item.getDesigner(), updateRequest.getDesigner());
		assertEquals(item.getBrand(), updateRequest.getBrand());
		assertEquals(item.getProductionYear(), updateRequest.getProductionYear());
		assertFalse(item.isAssessed());

		assertEquals(item.getImageItems().size(), 2);
		assertEquals(item.getImageItems().get(0).getUrl(), imageItems.get(0).getUrl());
		assertEquals(item.getImageItems().get(1).getUrl(), imageItems.get(1).getUrl());

		assertEquals(item.getImageChecks().size(), 1);
		assertEquals(item.getImageChecks().get(0).getUrl(), imageChecks.get(0).getUrl());
	}

	@DisplayName("04_01. update fail not found item")
	@Test
	public void test_04_01() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.empty());

		//when
		UpdateRequest updateRequest = getUpdateRequest();
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.update(
				updateRequest,
				getCategory("update category", root),
				getStyleItems(),
				imageItems,
				imageChecks));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	private List<StyleItem> getStyleItems() {
		List<StyleItem> styleItems = new ArrayList<>();
		styleItems.add(
			StyleItem.builder().style(
					Style.builder()
						.name("st1")
						.build())
				.item(null).build());

		styleItems.add(
			StyleItem.builder().style(
					Style.builder()
						.name("st2")
						.build())
				.item(null).build());

		return styleItems;
	}
}