package com.gokkan.gokkan.domain.category.service;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.dto.CategoryDto;
import com.gokkan.gokkan.domain.category.dto.CategoryDto.UpdateRequest;
import com.gokkan.gokkan.domain.category.exception.CategoryErrorCode;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Transactional
	public CategoryDto.Response create(CategoryDto.CreateRequest request) {
		checkParentNameAndChildName(request.getName(), request.getParent());
		duplicateCheck(request.getName());
		Category parent;
		Category category = request.toEntity();

		if (request.getParent() == null) {
			parent = categoryRepository.findByName("root")
				.orElseGet(() -> categoryRepository.save(Category.builder()
					.level(0)
					.parent(null)
					.children(new ArrayList<>())
					.name("root")
					.build()));
		} else {
			parent = getCategoryByName(request.getParent(), true);
		}

		category.setLevel(parent.getLevel() + 1);
		Category.addRelation(parent, category);

		return CategoryDto.Response.toResponse(categoryRepository.save(category));
	}

	@Transactional(readOnly = true)
	public CategoryDto.Response read(String name) {
		return CategoryDto.Response.toResponse(getCategoryByName(name, false));
	}

	@Transactional
	public boolean delete(String name) {
		categoryRepository.delete(getCategoryByName(name, false));
		return true;
	}

	@Transactional
	public CategoryDto.Response update(UpdateRequest request) {
		checkParentNameAndChildName(request.getName(), request.getParent());
		duplicateCheck(request.getName());
		Category category = getCategoryById(request.getId());

		if (!category.getParent().getName().equals(request.getParent())) {
			Category updateParent = getCategoryByName(request.getParent(), true);

			category.setParent(updateParent);
			category.setLevel(updateParent.getLevel() + 1);
			Category.addRelation(updateParent, category);
		}

		category.setName(request.getName());

		return CategoryDto.Response.toResponse(categoryRepository.save(category));
	}


	public Category getCategory(String categoryName) {
		return getCategoryByName(categoryName, false);
	}

	private void duplicateCheck(String request) {
		if (categoryRepository.existsByName(request)) {
			throw new RestApiException(CategoryErrorCode.DUPLICATED_CATEGORY);
		}
	}

	private Category getCategoryById(long id) {
		return categoryRepository.findById(id)
			.orElseThrow(() -> new RestApiException(CategoryErrorCode.NOT_FOUND_CATEGORY));
	}

	private Category getCategoryByName(String name, boolean parent) {
		return parent ?
			categoryRepository.findByName(name)
				.orElseThrow(
					() -> new RestApiException(CategoryErrorCode.NOT_FOUND_PARENT_CATEGORY))
			: categoryRepository.findByName(name)
				.orElseThrow(
					() -> new RestApiException(CategoryErrorCode.NOT_FOUND_CATEGORY));
	}

	private static void checkParentNameAndChildName(String name, String parent) {
		if (name.equals(parent)) {
			throw new RestApiException(CategoryErrorCode.CAN_NOT_SAME_PARENT_NAME);
		}
	}
}
