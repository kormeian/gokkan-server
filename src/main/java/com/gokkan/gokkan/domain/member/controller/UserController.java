package com.gokkan.gokkan.domain.member.controller;

import com.gokkan.gokkan.domain.member.domain.User;
import com.gokkan.gokkan.domain.member.service.UserService;
import com.gokkan.gokkan.global.security.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ApiResponse getUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User user = userService.getUser(principal.getUsername());

		return ApiResponse.success("user", user);
	}
}

