package com.gokkan.gokkan.domain.style.controller;

import com.gokkan.gokkan.domain.style.service.ItemStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemStyleController {

	private final ItemStyleService itemStyleService;

}
