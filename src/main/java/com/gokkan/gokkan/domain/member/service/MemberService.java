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

	@Transactional(readOnly = true)
	public Member getUser(String userId) {
		return memberRepository.findByUserId(userId);
	}

	@Transactional
	public void updateMember(Member member, RequestUpdateDto requestUpdateDto,
		List<MultipartFile> profileImage) {
		log.info("멤버 수정 시작");
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setEmail(requestUpdateDto.getEmail());
		member.setName(requestUpdateDto.getName());
		member.setPhoneNumber(requestUpdateDto.getPhoneNumber());
		member.setAddress(requestUpdateDto.getAddress());
		member.setCardNumber(requestUpdateDto.getCardNumber());
		if (profileImage != null) {
			String saveImage = awsS3Service.save(profileImage.get(0));
			member.setProfileImageUrl(saveImage);
		} else {
			member.setProfileImageUrl(member.getProfileImageUrl());
		}

		memberRepository.save(member);
		log.info("멤버 수정 완료");
	}

	@Transactional
	public void updateCard(Member member, String cardNumber) {
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setCardNumber(cardNumber);
		memberRepository.save(member);

	}

	@Transactional
	public void updateAddress(Member member, String address) {
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setAddress(address);
		memberRepository.save(member);
	}

	public boolean checkDuplicateNickName(String nickName) {
		return memberRepository.existsByName(nickName);
	}
}

