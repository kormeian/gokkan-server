package com.gokkan.gokkan.domain.expertInfo.controller;

import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByMemberId;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByNickName;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.ResponseGetExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.service.ExpertInfoService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expertInfo")
@RequiredArgsConstructor
public class ExpertInfoController {

	private final ExpertInfoService expertInfoService;

	@PostMapping
	public void createExpertInfoByMemberId(
		@RequestBody RequestCreateExpertInfoByMemberId requestCreateExpertInfoByMemberId) {
		expertInfoService.createExpertInfoByMemberId(requestCreateExpertInfoByMemberId);
	}

	@PostMapping("/nickName")
	public void createExpertInfoByName(
		@RequestBody RequestCreateExpertInfoByNickName requestCreateExpertInfoByNickName) {
		expertInfoService.createExpertInfoByNickName(requestCreateExpertInfoByNickName);
	}

	@PatchMapping("/myInfo")
	public void updateExpertInfo(@CurrentMember Member member, @RequestParam String info) {
		expertInfoService.updateExpertInfo(member, info);
	}

	@GetMapping("/myInfo")
	public ResponseEntity<ResponseGetExpertInfo> getExpertInfo(@CurrentMember Member member) {
		return ResponseEntity.ok(expertInfoService.getExpertInfo(member));
	}

}
