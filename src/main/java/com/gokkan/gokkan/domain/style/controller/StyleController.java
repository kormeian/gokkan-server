package com.gokkan.gokkan.domain.style.controller;

import com.gokkan.gokkan.domain.style.dto.StyleDto;
import com.gokkan.gokkan.domain.style.service.StyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("styles")
public class StyleController {

	private final StyleService styleService;

	@PostMapping("")
	public ResponseEntity<?> create(@RequestBody StyleDto.CreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(styleService.create(request));
	}

	@GetMapping("")
	public ResponseEntity<?> read(@RequestParam String name) {
		return ResponseEntity.status(HttpStatus.OK).body(styleService.read(name));
	}

	@DeleteMapping("")
	public ResponseEntity<?> delete(@RequestParam String name) {
		return ResponseEntity.status(HttpStatus.OK).body(styleService.delete(name));
	}

	@PutMapping("")
	public ResponseEntity<?> update(@RequestBody StyleDto.UpdateRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(styleService.update(request));
	}
}
