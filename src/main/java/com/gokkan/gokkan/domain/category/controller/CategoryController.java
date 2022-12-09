package com.gokkan.gokkan.domain.category.controller;

import com.gokkan.gokkan.domain.category.dto.CategoryDto;
import com.gokkan.gokkan.domain.category.service.CategoryService;
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
@RequestMapping("/category")
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping("")
	public ResponseEntity<?> create(@RequestBody CategoryDto.CreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
	}

	@GetMapping("")
	public ResponseEntity<?> read(@RequestParam String name) {
		return ResponseEntity.ok(categoryService.read(name));
	}

	@DeleteMapping("")
	public ResponseEntity<?> delete(@RequestParam String name) {
		return ResponseEntity.ok(categoryService.delete(name));

	}

	@PutMapping("")
	public ResponseEntity<?> update(@RequestBody CategoryDto.UpdateRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(request));
	}
}
