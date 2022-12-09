package com.gokkan.gokkan.domain.style.controller;

import com.gokkan.gokkan.domain.style.service.StyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StyleController {

	private final StyleService styleService;

}
