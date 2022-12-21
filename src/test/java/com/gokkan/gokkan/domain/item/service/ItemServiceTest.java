package com.gokkan.gokkan.domain.item.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.service.CategoryService;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import com.gokkan.gokkan.domain.image.repository.ImageItemRepository;
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
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.service.StyleItemService;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import java.io.FileInputStream;
import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
	Category root = Category.builder()
		.id(0L)
		.parent(null)
		.level(0)
		.name("root")
		.children(new ArrayList<>())
		.build();
	List<ImageItem> imageItems = List.of(getImageItem("item1"));
	List<ImageCheck> imageChecks = List.of(getImageCheck("check1"), getImageCheck("check2"));
	List<StyleItem> styleItems = List.of(getStyleItem("style1"), getStyleItem("style2"));
	List<String> styleNames = List.of("style1", "style2");
	String png = "png";
	Member member = Member.builder()
		.userId("userId")
		.email("member@email.com")
		.name("name")
		.providerType(ProviderType.KAKAO)
		.build();
	@Mock
	private ItemRepository itemRepository;
	@Mock
	private ImageItemRepository imageItemRepository;
	@Mock
	private ImageCheckRepository imageCheckRepository;
	@Mock
	private StyleItemRepository styleItemRepository;
	@Mock
	private CategoryService categoryService;
	@Mock
	private StyleItemService styleItemService;
	@Mock
	private ImageItemService imageItemService;
	@Mock
	private ImageCheckService imageCheckService;
	@InjectMocks
	private ItemService itemService;

	private static List<StyleItem> getStyleItems(List<String> styles) {
		List<StyleItem> styleItems = new ArrayList<>();
		for (String style : styles) {
			styleItems.add(StyleItem.builder()
				.style(Style.builder()
					.name(style)
					.build()
				)
				.build());
		}
		return styleItems;
	}

	@DisplayName("01_00. create success")
	@Test
	public void test_01_00() throws IOException {
		//given
		given(categoryService.getCategory(anyString())).willReturn(
			getCategory("test category", root));
		given(styleItemService.create(anyList())).willReturn(styleItems);
		given(imageItemService.create(anyList())).willReturn(imageItems);
		given(imageCheckService.create(anyList())).willReturn(imageChecks);
		given(itemRepository.save(any())).willReturn(getItem(imageItems, imageChecks));

		given(styleItemRepository.saveAll(any())).willReturn(styleItems);
		given((imageItemRepository.saveAll(any()))).willReturn(imageItems);
		given((imageCheckRepository.saveAll(any()))).willReturn(imageChecks);

		//when
		CreateRequest createRequest = getCreateRequest();
		itemService.create(createRequest, getMultipartFiles(png), getMultipartFiles(png), member);
		verify(itemRepository, times(2)).save(itemCaptor.capture());

		//then
		Item item = itemCaptor.getAllValues().get(1);
		System.out.println(item);
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

		assertEquals(item.getImageChecks().size(), 2);
		assertEquals(item.getImageChecks().get(0).getUrl(), imageChecks.get(0).getUrl());
		assertEquals(item.getImageChecks().get(1).getUrl(), imageChecks.get(1).getUrl());
		assertEquals(item.getImageChecks().get(0).getItem().getName(), createRequest.getName());

		assertEquals(item.getImageItems().size(), 1);
		assertEquals(item.getImageItems().get(0).getUrl(), imageItems.get(0).getUrl());
		assertEquals(item.getImageItems().get(0).getItem().getName(), createRequest.getName());

		assertEquals(item.getStyleItems().get(0).getStyle().getName(), styleNames.get(0));
		assertEquals(item.getStyleItems().get(1).getStyle().getName(), styleNames.get(1));
		assertEquals(item.getStyleItems().get(0).getItem().getName(), createRequest.getName());

		assertEquals(item.getMember().getName(), member.getName());
		assertEquals(item.getMember().getEmail(), member.getEmail());
	}

	@DisplayName("01_01. create fail not login")
	@Test
	public void test_01_01() {
		//given

		//when
		CreateRequest createRequest = getCreateRequest();
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.create(createRequest, getMultipartFiles(png), getMultipartFiles(png),
				null));

		//then
		assertEquals(restApiException.getErrorCode(), MemberErrorCode.MEMBER_NOT_LOGIN);
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
		System.out.println(response);
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
		assertEquals(response.getStyles().get(0), styleNames.get(0));
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
		boolean deleted = itemService.delete(1L, member);
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
			() -> itemService.delete(1L, member));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	@DisplayName("03_02. delete fail member not login")
	@Test
	public void test_03_02() {
		//given

		//when
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.delete(1L, null));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), MemberErrorCode.MEMBER_NOT_LOGIN);
	}

	@DisplayName("03_03. delete fail member mismatch")
	@Test
	public void test_03_03() {
		//given
		given(itemRepository.findById(anyLong())).willReturn(
			Optional.of(getItem(imageItems, imageChecks)));

		//when
		Member loginMember = Member.builder()
			.userId("mismatch")
			.email("member@email.com")
			.name("name")
			.providerType(ProviderType.KAKAO)
			.build();

		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.delete(1L, loginMember));
		verify(itemRepository, times(0)).delete(itemCaptor.capture());

		//then
		assertEquals(itemException.getErrorCode(), MemberErrorCode.MEMBER_MISMATCH);
	}

	@DisplayName("04_00. update success")
	@Test
	public void test_04_00() throws IOException {
		//given
		Item save = getItem(imageItems, imageChecks);
		save.setStyleItems(new ArrayList<>(List.of(
			StyleItem.builder()
				.id(1L)
				.item(save)
				.style(Style.builder().name("style1").build())
				.build())));

		given(itemRepository.findById(anyLong())).willReturn(Optional.of(save));

		given(categoryService.getCategory(anyString())).willReturn(
			getCategory("update category", root));
		styleItems = List.of(getStyleItem("update style 1"));
		given(styleItemService.create(anyList())).willReturn(styleItems);
		imageItems = List.of(getImageItem("update imageItem1"), getImageItem("update imageItem2"));
		given(imageItemService.create(anyList())).willReturn(imageItems);
		imageChecks = List.of((getImageCheck("update imageCheck1")));
		given(imageCheckService.create(anyList())).willReturn(imageChecks);
		given(itemRepository.save(any())).willReturn(getItem(imageItems, imageChecks));

		given(styleItemRepository.saveAll(any())).willReturn(styleItems);
		given((imageItemRepository.saveAll(any()))).willReturn(imageItems);
		given((imageCheckRepository.saveAll(any()))).willReturn(imageChecks);

		//when
		UpdateRequest updateRequest = getUpdateRequest();
		itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png), member);
		verify(itemRepository, times(1)).save(itemCaptor.capture());
		verify(imageItemService, times(1)).delete(any(ImageItem.class));
		verify(imageCheckService, times(2)).delete(any(ImageCheck.class));

		//then
		Item item = itemCaptor.getValue();
		System.out.println(item);
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

		assertEquals(item.getImageItems().size(), 2);
		assertEquals(item.getImageItems().get(0).getUrl(), imageItems.get(0).getUrl());
		assertEquals(item.getImageItems().get(1).getUrl(), imageItems.get(1).getUrl());

		assertEquals(item.getImageChecks().size(), 1);
		assertEquals(item.getImageChecks().get(0).getUrl(), imageChecks.get(0).getUrl());
		assertEquals(item.getStyleItems().get(0).getStyle().getName(),
			styleItems.get(0).getStyle().getName());
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
			() -> itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				member));

		//then
		assertEquals(itemException.getErrorCode(), ItemErrorCode.NOT_FOUND_ITEM);
	}

	@DisplayName("04_02. update fail member not login")
	@Test
	public void test_04_02() {
		//given

		//when
		UpdateRequest updateRequest = getUpdateRequest();
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				null));

		//then
		assertEquals(itemException.getErrorCode(), MemberErrorCode.MEMBER_NOT_LOGIN);
	}

	@DisplayName("04_03. update fail member mismatch")
	@Test
	public void test_04_03() {
		//given
		Item save = getItem(imageItems, imageChecks);
		save.setStyleItems(new ArrayList<>(List.of(
			StyleItem.builder()
				.id(1L)
				.item(save)
				.style(Style.builder().name("style1").build())
				.build())));

		given(itemRepository.findById(anyLong())).willReturn(Optional.of(save));

		//when
		Member loginMember = Member.builder()
			.userId("mismatch")
			.email("member@email.com")
			.name("name")
			.providerType(ProviderType.KAKAO)
			.build();
		UpdateRequest updateRequest = getUpdateRequest();
		RestApiException itemException = assertThrows(RestApiException.class,
			() -> itemService.update(updateRequest, getMultipartFiles(png), getMultipartFiles(png),
				loginMember));

		//then
		assertEquals(itemException.getErrorCode(), MemberErrorCode.MEMBER_MISMATCH);
	}

	@DisplayName("05_00. create temporary success")
	@Test
	public void test_05_00(){
	    //given
		given(itemRepository.save(any())).willReturn(
			Item.builder().id(1L).member(member).state(State.TEMPORARY).build());
	    //when
		itemService.createTemporary(member);
		verify(itemRepository, times(1)).save(itemCaptor.capture());

	    //then
		Item item = itemCaptor.getValue();
		assertEquals(item.getMember().getUserId(), member.getUserId());
		assertEquals(item.getState(), State.TEMPORARY);
	}

	@DisplayName("05_01. create temporary fail not login")
	@Test
	public void test_05_01(){
		//given

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> itemService.createTemporary(null));

		//then
		assertEquals(restApiException.getErrorCode(), MemberErrorCode.MEMBER_NOT_LOGIN);
	}

	Category root = Category.builder()
		.id(0L)
		.parent(null)
		.level(0)
		.name("root")
		.children(new ArrayList<>())
		.build();
	List<ImageItem> imageItems = List.of(getImageItem("item1"));
	List<ImageCheck> imageChecks = List.of(getImageCheck("check1"), getImageCheck("check2"));
	List<StyleItem> styleItems = List.of(getStyleItem("style1"), getStyleItem("style2"));

	List<String> styleNames = List.of("style1", "style2");

	String png = "png";

	Member member = Member.builder()
		.userId("userId")
		.email("member@email.com")
		.name("name")
		.providerType(ProviderType.KAKAO)
		.build();

	private static Category getCategory(String name, Category parent) {
		return Category.builder()
			.name(name)
			.parent(parent)
			.children(new ArrayList<>())
			.level(parent.getLevel() + 1)
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

	private Item getItem(List<ImageItem> imageItems, List<ImageCheck> imageChecks) {
		return Item.builder()
			.name("test name")
			.member(member)
			.category(getCategory("test category", root))
			.startPrice(100L)
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
			.styleItems(new ArrayList<>(getStyleItems(styleNames)))
			.build();
	}

	private StyleItem getStyleItem(String styleName) {
		return StyleItem.builder().style(Style.builder()
				.name(styleName)
				.build())
			.build();
	}

	private static List<StyleItem> getStyleItems(List<String> styles) {
		List<StyleItem> styleItems = new ArrayList<>();
		for (String style : styles) {
			styleItems.add(StyleItem.builder()
				.style(Style.builder()
					.name(style)
					.build()
				)
				.build());
		}
		return styleItems;
	}

	private List<MultipartFile> getMultipartFiles(String extension) throws IOException {
		List<MultipartFile> multipartFile = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			String file = String.format("%d.%s", i, extension);
			FileInputStream fis = new FileInputStream("src/main/resources/testImages/" + file);
			multipartFile.add(new MockMultipartFile(String.format("%d", i), file, extension, fis));
		}
		return multipartFile;
	}


	private CreateRequest getCreateRequest() {
		return ItemDto.CreateRequest.builder()
			.name("test name")
			.category("test category")
			.startPrice(100L)
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
			.styles(styleNames)
			.build();
	}

	private static UpdateRequest getUpdateRequest() {
		return ItemDto.UpdateRequest.builder()
			.itemId(1L)
			.name("update name")
			.category("update category")
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
			.styles(List.of("update style 1"))
			.build();
	}
}