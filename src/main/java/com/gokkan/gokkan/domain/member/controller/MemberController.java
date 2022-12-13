package com.gokkan.gokkan.domain.member.controller;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.domain.MemberAdapter;
import com.gokkan.gokkan.domain.member.service.UserService;
import com.gokkan.gokkan.global.security.common.ApiResponse;
import com.gokkan.gokkan.global.security.oauth.entity.UserPrincipal;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

	private final UserService userService;

	@GetMapping
	public ApiResponse getUser() {
		MemberAdapter principal = (MemberAdapter) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();

		Member member = principal.getMember();

		return ApiResponse.success("member", member);
	}

	@GetMapping("/test")
	public ApiResponse test(@CurrentMember Member member) {

		System.out.println(member.getUserId());
		return ApiResponse.success("test", "test");
	}
}

