package com.gokkan.gokkan.domain.style.controller;

import com.gokkan.gokkan.domain.style.service.ExpertStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/styles/items")
public class ExpertStyleController {

	private final ExpertStyleService expertStyleService;

}
