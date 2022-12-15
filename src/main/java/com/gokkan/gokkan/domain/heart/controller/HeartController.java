package com.gokkan.gokkan.domain.heart.controller;

import com.gokkan.gokkan.domain.heart.service.HeartService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hearts")
public class HeartController {

	private final HeartService heartService;
}
