package com.gokkan.gokkan.domain.member.service;

import com.gokkan.gokkan.domain.image.service.AwsS3Service;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.domain.dto.MemberDto.RequestUpdateDto;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AwsS3Service awsS3Service;

	@Transactional
	public void updateMember(Member member, RequestUpdateDto requestUpdateDto,
		MultipartFile profileImage) {
		log.info("멤버 수정 시작 이름 : " + member.getName());
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setName(requestUpdateDto.getName());
		member.setNickName(requestUpdateDto.getNickName());
		member.setPhoneNumber(requestUpdateDto.getPhoneNumber());
		member.setAddress(requestUpdateDto.getAddress());
		member.setAddressDetail(requestUpdateDto.getAddressDetail());
		member.setCardNumber(requestUpdateDto.getCardNumber());
		if (profileImage != null && !profileImage.isEmpty() && profileImage.getSize() > 0) {
			String saveImage = awsS3Service.save(profileImage);
			member.setProfileImageUrl(saveImage);
			log.info("프로필 이미지 수정 완료");
		} else {
			member.setProfileImageUrl(member.getProfileImageUrl());
		}

		memberRepository.save(member);
		log.info("멤버 수정 완료");
	}

	@Transactional
	public void updateCard(Member member, String cardNumber) {
		log.info("카드 수정 시작 이름 : " + member.getName());
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setCardNumber(cardNumber);
		memberRepository.save(member);
		log.info("카드 수정 완료");
	}

	@Transactional
	public void updateAddress(Member member, String address, String addressDetail) {
		log.info("주소 수정 시작 이름 : " + member.getName());
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setAddress(address);
		member.setAddressDetail(addressDetail);
		memberRepository.save(member);
		log.info("주소 수정 완료");
	}

	@Transactional(readOnly = true)
	public boolean checkDuplicateNickName(String nickName) {
		log.info("닉네임 중복 체크");
		return memberRepository.existsByNickName(nickName);
	}
}

