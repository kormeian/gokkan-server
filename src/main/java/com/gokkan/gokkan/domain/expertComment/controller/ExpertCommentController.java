package com.gokkan.gokkan.domain.expertComment.controller;

import com.gokkan.gokkan.domain.expertComment.domain.dto.ExpertCommentDto.RequestCreateExpertComment;
import com.gokkan.gokkan.domain.expertComment.service.ExpertCommentService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "전문가 의견 컨트롤러", description = "전문가 의견 컨트롤러")
@RestController
@RequestMapping("/api/v1/expert/comment")
@RequiredArgsConstructor
public class ExpertCommentController {

	private final ExpertCommentService expertCommentService;

	@PostMapping
	@Operation(summary = "전문가 의견 생성", description = "전문가 의견 생성")
	public void createExpertComment(
		@Parameter(hidden = true) @CurrentMember Member member,
		@Parameter(content = @Content(schema = @Schema(implementation = RequestCreateExpertComment.class)))
		@RequestBody RequestCreateExpertComment requestCreateExpertComment) {
		expertCommentService.createExpertComment(member, requestCreateExpertComment);
	}
}
