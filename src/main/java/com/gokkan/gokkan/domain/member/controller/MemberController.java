package com.gokkan.gokkan.domain.member.controller;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.service.UserService;
import com.gokkan.gokkan.global.security.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
		User principal = (User) SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal();

		Member member = userService.getUser(principal.getUsername());

		return ApiResponse.success("member", member);
	}
}

