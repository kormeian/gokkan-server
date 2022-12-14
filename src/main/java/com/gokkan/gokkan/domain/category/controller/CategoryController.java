package com.gokkan.gokkan.domain.category.controller;

import com.gokkan.gokkan.domain.category.dto.CategoryDto;
import com.gokkan.gokkan.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

	@Operation(summary = "카테고리 생성", description = "카테고리 생성")
	@ApiResponse(responseCode = "201", description = "생성된 카테고리 반환", content = @Content(schema = @Schema(implementation = CategoryDto.Response.class)))
	@PostMapping("")
	public ResponseEntity<?> create(
		@Parameter(description = "카테고리 생성 request", required = true, content = @Content(schema = @Schema(implementation = CategoryDto.CreateRequest.class)))
		@RequestBody CategoryDto.CreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
	}

	@Operation(summary = "카테고리 읽기", description = "이름에 해당하는 카테고리 반환")
	@ApiResponse(responseCode = "200", description = "이름에 해당하는 카테고리 반환", content = @Content(schema = @Schema(implementation = CategoryDto.Response.class)))
	@GetMapping("")
	public ResponseEntity<?> read(
		@Parameter(description = "카테고리 조회 parameter", required = true)
		@RequestParam String name) {
		return ResponseEntity.ok(categoryService.read(name));
	}

	@Operation(summary = "카테고리 삭제", description = "이름에 해당하는 카테고리 삭제")
	@ApiResponse(responseCode = "200", description = "이름에 해당하는 카테고리 삭제, 하위 카테고리까지 삭제")
	@DeleteMapping("")
	public ResponseEntity<?> delete(
		@Parameter(description = "카테고리 삭제 parameter", required = true)
		@RequestParam String name) {
		return ResponseEntity.ok(categoryService.delete(name));

	}

	@Operation(summary = "카테고리 수정", description = "카테고리 수정")
	@ApiResponse(responseCode = "200", description = "수정된 카테고리 반환", content = @Content(schema = @Schema(implementation = CategoryDto.Response.class)))
	@PutMapping("")
	public ResponseEntity<?> update(
		@Parameter(description = "카테고리 수정 request", required = true, content = @Content(schema = @Schema(implementation = CategoryDto.UpdateRequest.class)))
		@RequestBody CategoryDto.UpdateRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(request));
	}
}
