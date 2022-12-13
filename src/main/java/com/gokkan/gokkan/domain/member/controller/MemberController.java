package com.gokkan.gokkan.domain.member.controller;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.domain.MemberAdapter;
import com.gokkan.gokkan.domain.member.domain.dto.MemberDto.RequestUpdateDto;
import com.gokkan.gokkan.domain.member.service.MemberService;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<Member> getUser() {
		MemberAdapter principal = (MemberAdapter) SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();

		Member member = principal.getMember();

		return ResponseEntity.ok(member);
	}

	@PatchMapping("/update")
	public ResponseEntity<Void> updateMember(@CurrentMember Member member,
		@RequestBody RequestUpdateDto requestUpdateDto,
		@RequestPart MultipartFile profileImage) {

		return memberService.updateMember(member, requestUpdateDto, profileImage);
	}

	@PatchMapping("/update/card")
	public ResponseEntity<Void> updateCard(@CurrentMember Member member,
		@RequestParam String cardNumber) {

		return memberService.updateCard(member, cardNumber);
	}

	@PatchMapping("/update/address")
	public ResponseEntity<Void> updateAddress(@CurrentMember Member member,
		@RequestParam String address) {

		return memberService.updateAddress(member, address);
	}
}

