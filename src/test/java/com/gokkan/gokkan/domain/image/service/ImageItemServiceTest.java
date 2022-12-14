package com.gokkan.gokkan.domain.image.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.exception.ImageErrorCode;
import com.gokkan.gokkan.domain.image.exception.ImageException;
import com.gokkan.gokkan.domain.image.repository.ImageItemRepository;
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
class ImageItemServiceTest {

	static List<String> urls = List.of("u1", "u2", "u3");
	static ArgumentCaptor<ImageItem> imageItemCaptor = ArgumentCaptor.forClass(ImageItem.class);
	@Mock
	private ImageItemRepository imageItemRepository;
	@Mock
	private AwsS3Service awsS3Service;
	@InjectMocks
	private ImageItemService imageItemService;

	private static ImageItem getImageItem(String url) {
		return ImageItem.builder()
			.url(url)
			.build();
	}

	@DisplayName("01_00. save success")
	@Test
	public void test_01_00() {
		//given
		for (String url : urls) {
			ImageItem imageItem = getImageItem(url);
			lenient().when(imageItemRepository.save(imageItem)).thenReturn(imageItem);
		}

		//when
		imageItemService.save(urls);
		verify(imageItemRepository, times(3)).save(imageItemCaptor.capture());

		//then
		List<ImageItem> imageItems = imageItemCaptor.getAllValues();
		for (int i = 0; i < urls.size(); i++) {
			assertEquals(imageItems.get(i).getUrl(), urls.get(i));
		}
	}

	@DisplayName("01_02. save fail empty url")
	@Test
	public void test_01_02() {
		//given

		//when
		ImageException imageException = assertThrows(ImageException.class,
			() -> imageItemService.save(new ArrayList<>()));

		//then
		assertEquals(imageException.getErrorCode(), ImageErrorCode.EMPTY_URL);
	}

	@DisplayName("01_03. save fail invalid url")
	@Test
	public void test_01_03() {
		//given

		//when
		ImageException imageException = assertThrows(ImageException.class,
			() -> imageItemService.save(new ArrayList<>(List.of("", ""))));

		//then
		assertEquals(imageException.getErrorCode(), ImageErrorCode.INVALID_FORMAT_URL);
	}

	@DisplayName("02_00. delete success")
	@Test
	public void test_02_00() {
		//given
		given(imageItemRepository.findById(1L))
			.willReturn(Optional.of(getImageItem("test")));

		//when
		boolean deleted = imageItemService.delete(1L);
		verify(imageItemRepository, times(1)).delete(any());
		verify(awsS3Service, times(1)).delete(any());

		//then
		assertTrue(deleted);
	}

	@DisplayName("02_01. delete fail not found")
	@Test
	public void test_02_01() {
		//given
		given(imageItemRepository.findById(1L))
			.willReturn(Optional.empty());

		//when
		ImageException imageException = assertThrows(ImageException.class,
			() -> imageItemService.delete(1L));
		verify(imageItemRepository, times(0)).delete(any());
		verify(awsS3Service, times(0)).delete(any());

		//then
		assertEquals(imageException.getErrorCode(), ImageErrorCode.NOT_FOUND_IMAGE_ITEM);
	}

}