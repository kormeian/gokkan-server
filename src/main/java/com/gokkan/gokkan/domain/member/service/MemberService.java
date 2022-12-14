package com.gokkan.gokkan.domain.member.service;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.domain.dto.MemberDto.RequestUpdateDto;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Member getUser(String userId) {
		return memberRepository.findByUserId(userId);
	}

	@Transactional
	public ResponseEntity<Void> updateMember(Member member, RequestUpdateDto requestUpdateDto,
		MultipartFile profileImage) {
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setEmail(requestUpdateDto.getEmail());
		member.setName(requestUpdateDto.getName());
		member.setPhoneNumber(requestUpdateDto.getPhoneNumber());
		member.setAddress(requestUpdateDto.getAddress());
		member.setCardNumber(requestUpdateDto.getCardNumber());
		if (profileImage != null) {
			//TODO: S3에 이미지 업로드
			member.setProfileImageUrl(profileImage.getName());
		} else {
			member.setProfileImageUrl(member.getProfileImageUrl());
		}

		memberRepository.save(member);

		return ResponseEntity.ok().build();
	}

	@Transactional
	public ResponseEntity<Void> updateCard(Member member, String cardNumber) {
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setCardNumber(cardNumber);
		memberRepository.save(member);

		return ResponseEntity.ok().build();
	}

	@Transactional
	public ResponseEntity<Void> updateAddress(Member member, String address) {
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.setAddress(address);
		memberRepository.save(member);

		return ResponseEntity.ok().build();
	}
}

