package com.gokkan.gokkan.domain.style.controller;

import com.gokkan.gokkan.domain.style.service.ExpertStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expert/style")
@RequiredArgsConstructor
public class ExpertStyleController {
	private final ExpertStyleService expertStyleService;

	@PostMapping
	public void createStyleByStyleId(
		@RequestParam Long expertInfoId,
		@RequestParam Long styleId){
		expertStyleService.createStyleByStyleId(expertInfoId, styleId);
	}

	@PostMapping
	public void createStyleByStyleName(
		@RequestParam Long expertInfoId,
		@RequestParam String styleName){
		expertStyleService.createStyleByStyleName(expertInfoId, styleName);
	}

	@DeleteMapping
	public void deleteExpertStyle(
		@RequestParam Long expertStyleId){
		expertStyleService.deleteExpertStyle(expertStyleId);
	}

	@DeleteMapping
	public void deleteStyleByStyleName(
		@RequestParam Long expertInfoId,
		@RequestParam String styleName){
		expertStyleService.deleteStyleByStyleName(expertInfoId, styleName);
	}

}
