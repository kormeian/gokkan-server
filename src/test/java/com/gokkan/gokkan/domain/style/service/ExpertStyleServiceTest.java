package com.gokkan.gokkan.domain.style.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.ExpertStyleRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpertStyleServiceTest {
	@Mock
	private ExpertStyleRepository expertStyleRepository;
	@Mock
	private StyleRepository styleRepository;
	@Mock
	private ExpertInfoRepository expertInfoRepository;
	@InjectMocks
	private ExpertStyleService expertStyleService;

	ArgumentCaptor<ExpertStyle> expertStyleArgumentCaptor = ArgumentCaptor.forClass(ExpertStyle.class);
	final static ExpertInfo expertInfo = ExpertInfo.TestOnlyBuilder()
		.id(1L)
		.build();

	final static Style style = Style.builder()
		.id(1L)
		.name("test")
		.build();

	@Test
	@DisplayName("전문가 스타일 생성(스타일 id) 성공")
	void createStyleByStyleId_success() {
	    //given
	    given(expertInfoRepository.findById(1L)).willReturn(Optional.of(expertInfo));
		given(styleRepository.findById(1L)).willReturn(Optional.of(style));
	    //when
		expertStyleService.createStyleByStyleId(1L, 1L);
	    //then
		verify(expertStyleRepository).save(expertStyleArgumentCaptor.capture());
		ExpertStyle expertStyle = expertStyleArgumentCaptor.getValue();
		assertEquals(expertStyle.getExpertInfo(), expertInfo);
		assertEquals(expertStyle.getStyle(), style);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 id) 실패 - 전문가 정보 없음")
	void createStyleByStyleId_error_notFoundExpertInfo() {
	    //given
	    given(expertInfoRepository.findById(1L)).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {expertStyleService.createStyleByStyleId(1L, 1L);});

		//then
		assertEquals(restApiException.getErrorCode(), ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 id) 실패 - 스타일 정보 없음")
	void createStyleByStyleId_error_notFoundStyle() {
	    //given
	    given(styleRepository.findById(1L)).willReturn(Optional.empty());

		//when
	    RestApiException restApiException = assertThrows(RestApiException.class, () -> {expertStyleService.createStyleByStyleId(1L, 1L);});

	    //then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 이름) 성공")
	void createStyleByStyleName() {
	    //given
	    given(expertInfoRepository.findById(1L)).willReturn(Optional.of(expertInfo));
		given(styleRepository.findByName("test")).willReturn(Optional.of(style));

	    //when
	    expertStyleService.createStyleByStyleName(1L, "test");

	    //then
		verify(expertStyleRepository).save(expertStyleArgumentCaptor.capture());
		ExpertStyle expertStyle = expertStyleArgumentCaptor.getValue();
		assertEquals(expertStyle.getExpertInfo(), expertInfo);
		assertEquals(expertStyle.getStyle(), style);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 이름) 실패 - 전문가 정보 없음")
	void createStyleByStyleName_error_notFoundExpertInfo() {
		//given
		given(expertInfoRepository.findById(1L)).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {expertStyleService.createStyleByStyleId(1L, 1L);});

		//then
		assertEquals(restApiException.getErrorCode(), ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND);
	}

	@Test
	@DisplayName("전문가 스타일 생성(스타일 이름) 실패 - 스타일 정보 없음")
	void createStyleByStyleName_error_notFoundStyle() {
		//given
		given(styleRepository.findByName("test")).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class, () -> {expertStyleService.createStyleByStyleId(1L, 1L);});

		//then
		assertEquals(restApiException.getErrorCode(), StyleErrorCode.NOT_FOUND_STYLE);
	}

	@Test
	@DisplayName("전문가 스타일 삭제 성공")
	void deleteExpertStyle() {
	    //given

	    //when

	    //then
	}
}