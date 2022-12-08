package com.gokkan.gokkan.domain.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.dto.CategoryDto;
import com.gokkan.gokkan.domain.category.dto.CategoryDto.CreateRequest;
import com.gokkan.gokkan.domain.category.dto.CategoryDto.Response;
import com.gokkan.gokkan.domain.category.exception.CategoryErrorCode;
import com.gokkan.gokkan.domain.category.exception.CategoryException;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryServiceTest {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryRepository categoryRepository;


	@DisplayName("01_00. create root category success")
	@Test
	public void test_01_00() {
		//given
		CategoryDto.CreateRequest request =
			CreateRequest.builder()
				.parent(null)
				.name("의자")
				.build();

		System.out.println("===============================");

		//when
		Response response = categoryService.create(request);

		//then
		Category savedCategory = categoryRepository.findById(response.getId()).get();

		assertEquals(response.getName(), request.getName());
		assertEquals(savedCategory.getParent().getName(), "root");
		assertEquals(savedCategory.getParent().getChildren().size(), 1);
		assertEquals(savedCategory.getLevel(), savedCategory.getParent().getLevel() + 1);
	}

	@DisplayName("01_01. create root category success already exist root")
	@Test
	public void test_01_01() {
		//given
		Category root = categoryRepository.save(Category.builder()
			.level(0)
			.name("root")
			.children(new ArrayList<>())
			.build());

		CategoryDto.CreateRequest request =
			CreateRequest.builder()
				.parent(null)
				.name("의자")
				.build();

		System.out.println("===============================");

		//when
		Response response = categoryService.create(request);

		//then
		Category savedCategory = categoryRepository.findById(response.getId()).get();

		assertEquals(response.getName(), request.getName());
		assertEquals(savedCategory.getParent().getName(), "root");
		assertEquals(savedCategory.getParent().getChildren().size(), 1);
		assertEquals(savedCategory.getLevel(), root.getLevel() + 1);
	}

	@DisplayName("01_02. create not root category success")
	@Test
	public void test_01_02() {
		//given
		Category parent = categoryRepository.save(Category.builder()
			.level(1)
			.name("의자")
			.children(new ArrayList<>())
			.build());

		CategoryDto.CreateRequest request =
			CreateRequest.builder()
				.parent("의자")
				.name("좌식 의자")
				.build();

		System.out.println("===============================");

		//when
		Response response = categoryService.create(request);

		//then
		Category savedCategory = categoryRepository.findById(response.getId()).get();

		assertEquals(response.getName(), request.getName());
		assertEquals(savedCategory.getParent().getName(), "의자");
		assertEquals(savedCategory.getParent().getChildren().size(), 1);
		assertEquals(savedCategory.getLevel(), parent.getLevel() + 1);
	}

	@DisplayName("01_03. create not root category fail not found parent")
	@Test
	public void test_01_03() {
		//given
		CategoryDto.CreateRequest request =
			CreateRequest.builder()
				.parent("의자")
				.name("좌식 의자")
				.build();

		System.out.println("===============================");

		//when
		CategoryException categoryException = assertThrows(CategoryException.class,
			() -> categoryService.create(request));

		//then
		assertEquals(categoryException.getErrorCode(), CategoryErrorCode.NOT_FOUND_PARENT_CATEGORY);
	}


}