package com.gokkan.gokkan.domain.style.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
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
class StyleItemServiceTest {

	private final String name1 = "test style1";
	private final String name2 = "test style2";
	private final List<String> names = List.of(name1, name2);
	ArgumentCaptor<StyleItem> styleItemCaptor = ArgumentCaptor.forClass(StyleItem.class);
	@Mock
	private StyleItemRepository styleItemRepository;
	@Mock
	private StyleRepository styleRepository;
	@InjectMocks
	private StyleItemService styleItemService;

	private static Item getItem() {
		return Item.builder()
			.name("test name")
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
			.created(LocalDateTime.now())
			.updated(LocalDateTime.now())
			.imageItems(new ArrayList<>())
			.imageChecks(new ArrayList<>())
			.build();
	}

	@DisplayName("01_00. create success")
	@Test
	public void test_01_00() {
		//given
		Style style = getStyle(name1);
		given(styleRepository.findByName(any())).willReturn(Optional.of(style));

		//when
		List<StyleItem> styleItems = styleItemService.create(names);

		verify(styleItemRepository, times(0)).save(styleItemCaptor.capture());

		//then
		assertEquals(styleItems.get(0).getStyle().getName(), names.get(0));
	}

	@DisplayName("01_01. create fail not found style")
	@Test
	public void test_01_01() {
		//given
		given(styleRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> styleItemService.create(names));

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@DisplayName("02_00. update success")
	@Test
	public void test_02_00() {
		//given
		Style style = getStyle(name1);
		given(styleItemRepository.findById(any())).willReturn(
			Optional.of(getStyleItem(style)));
		given(styleRepository.findByName(any())).willReturn(Optional.of(getStyle("update")));
		given(styleItemRepository.save(any())).willReturn(getStyleItem(style));

		//when
		styleItemService.update(1L, "update");
		verify(styleItemRepository, times(1)).save(
			styleItemCaptor.capture());

		//then
		StyleItem updateStyleItem = styleItemCaptor.getValue();
		assertEquals(updateStyleItem.getStyle().getName(), "update");
	}

	@DisplayName("02_01. update fail not found style")
	@Test
	public void test_02_01() {
		//given
		Style style = getStyle(name1);
		given(styleItemRepository.findById(any())).willReturn(
			Optional.of(getStyleItem(style)));
		given(styleRepository.findByName(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> styleItemService.update(1L, "update"));

		//then

		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@DisplayName("02_02. update fail not found style item")
	@Test
	public void test_02_02() {
		//given
		given(styleItemRepository.findById(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> styleItemService.update(1L, "update"));

		//then

		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE_ITEM);
	}

	private Style getStyle(String styleName) {
		return Style.builder()
			.name(styleName)
			.build();
	}

	private StyleItem getStyleItem(Style style) {
		return StyleItem.builder()
			.style(style)
			.item(getItem())
			.build();
	}
}