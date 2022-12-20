package com.gokkan.gokkan.domain.expertInfo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByMemberId;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByNickName;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.ResponseGetExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
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
class ExpertInfoServiceTest {
	ArgumentCaptor<ExpertInfo> expertInfoArgumentCaptor = ArgumentCaptor.forClass(ExpertInfo.class);
	@Mock
	private ExpertInfoRepository expertInfoRepository;
	@Mock
	private MemberRepository memberRepository;
	@InjectMocks
	private ExpertInfoService expertInfoService;

	final static Member member = Member.builder()
		.nickName("test")
		.build();

	@Test
	@DisplayName("전문가 정보 등록(멤버 아이디) 성공")
	void createExpertInfoByMemberId_success() {
		//given
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
		given(expertInfoRepository.existsByMemberId(member.getId())).willReturn(false);


		//when
		expertInfoService.createExpertInfoByMemberId(getRequestCreateExpertInfoByMemberId());

		//then
		verify(expertInfoRepository).save(expertInfoArgumentCaptor.capture());
		ExpertInfo expertInfo = expertInfoArgumentCaptor.getValue();
		assertEquals(expertInfo.getMember(), member);
		assertEquals(expertInfo.getName(), "name");
		assertEquals(expertInfo.getInfo(), "info");

	}

	@Test
	@DisplayName("전문가 정보 등록(멤버 아이디) 실패 - 멤버를 찾지 못함")
	void createExpertInfoByMemberId_error_memberNotFound() {
		//given
		given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> expertInfoService.createExpertInfoByMemberId(
				getRequestCreateExpertInfoByMemberId()));

		//then
		assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, restApiException.getErrorCode());
	}

	@Test
	@DisplayName("전문가 정보 등록(멤버 아이디) 실패 - 이미 등록된 멤버")
	void createExpertInfoByMemberId_error_alreadyExistMember() {
		//given
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
		given(expertInfoRepository.existsByMemberId(member.getId())).willReturn(true);

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> expertInfoService.createExpertInfoByMemberId(
				getRequestCreateExpertInfoByMemberId()));

		//then
		assertEquals(ExpertInfoErrorCode.ALREADY_EXIST_MEMBER, restApiException.getErrorCode());
	}

	@Test
	@DisplayName("전문가 정보 등록(닉네임) 성공")
	void createExpertInfoByNickName_success() {
		//given
		given(memberRepository.findByNickName(any())).willReturn(Optional.of(member));
		given(expertInfoRepository.existsByMemberId(member.getId())).willReturn(false);

		//when
		expertInfoService.createExpertInfoByNickName(getRequestCreateExpertInfoByNickName());

		//then
		verify(expertInfoRepository).save(expertInfoArgumentCaptor.capture());
		ExpertInfo expertInfo = expertInfoArgumentCaptor.getValue();
		assertEquals(expertInfo.getMember(), member);
		assertEquals(expertInfo.getName(), "name");
		assertEquals(expertInfo.getInfo(), "info");
	}

	@Test
	@DisplayName("전문가 정보 등록(닉네임) 실패 - 멤버를 찾지 못함")
	void createExpertInfoByNickName_error_memberNotFound() {
		//given
		given(memberRepository.findByNickName(any())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> expertInfoService.createExpertInfoByNickName(
				getRequestCreateExpertInfoByNickName()));

		//then
		assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, restApiException.getErrorCode());
	}

	@Test
	@DisplayName("전문가 정보 등록(닉네임) 실패 - 이미 등록된 멤버")
	void createExpertInfoByNickName_error_alreadyExistMember() {
		//given
		given(memberRepository.findByNickName(any())).willReturn(Optional.of(member));
		given(expertInfoRepository.existsByMemberId(member.getId())).willReturn(true);

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> expertInfoService.createExpertInfoByNickName(
				getRequestCreateExpertInfoByNickName()));

		//then
		assertEquals(ExpertInfoErrorCode.ALREADY_EXIST_MEMBER, restApiException.getErrorCode());
	}

	@Test
	@DisplayName("전문가 정보 수정 성공")
	void updateExpertInfo_success() {
		//given
		given(expertInfoRepository.findByMemberId(member.getId())).willReturn(
			Optional.of(getExpertInfo()));

		//when
		expertInfoService.updateExpertInfo(member, "updateInfo");

		//then
		verify(expertInfoRepository).save(expertInfoArgumentCaptor.capture());
		ExpertInfo expertInfo = expertInfoArgumentCaptor.getValue();
		assertEquals(expertInfo.getInfo(), "updateInfo");
	}

	@Test
	@DisplayName("전문가 정보 수정 실패 - 전문가 정보를 찾지 못함")
	void updateExpertInfo_error_expertInfoNotFound() {
		//given
		given(expertInfoRepository.findByMemberId(member.getId())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> expertInfoService.updateExpertInfo(member, "updateInfo"));

		//then
		assertEquals(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND, restApiException.getErrorCode());
	}

	@Test
	@DisplayName("전문가 정보 조회 성공")
	void getExpertInfo_success() {
		//given
		given(expertInfoRepository.findByMemberId(member.getId())).willReturn(
			Optional.of(getExpertInfo()));

		//when
		ResponseGetExpertInfo responseGetExpertInfo = expertInfoService.getExpertInfo(member);

		//then
		assertEquals("info", responseGetExpertInfo.getInfo());
		assertEquals("name", responseGetExpertInfo.getName());
	}

	@Test
	@DisplayName("전문가 정보 조회 실패 - 전문가 정보를 찾지 못함")
	void getExpertInfo_error_expertInfoNotFound() {
		//given
		given(expertInfoRepository.findByMemberId(member.getId())).willReturn(Optional.empty());

		//when
		RestApiException restApiException = assertThrows(RestApiException.class,
			() -> expertInfoService.getExpertInfo(member));

		//then
		assertEquals(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND, restApiException.getErrorCode());
	}

	private RequestCreateExpertInfoByMemberId getRequestCreateExpertInfoByMemberId() {
		return RequestCreateExpertInfoByMemberId.builder()
			.memberId(1L)
			.name("name")
			.info("info")
			.build();
	}

	private RequestCreateExpertInfoByNickName getRequestCreateExpertInfoByNickName() {
		return RequestCreateExpertInfoByNickName.builder()
			.nickName("test")
			.name("name")
			.info("info")
			.build();
	}

	private ExpertInfo getExpertInfo() {
		return ExpertInfo.builder()
			.name("name")
			.info("info")
			.member(member)
			.build();
	}
}