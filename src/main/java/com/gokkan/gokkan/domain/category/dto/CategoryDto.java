package com.gokkan.gokkan.domain.category.dto;

import com.gokkan.gokkan.domain.category.domain.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CategoryDto {

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class CreateRequest {

		private Long id;

		private String name;

		private String parent;


		public Category toEntity() {
			return Category.builder()
				.name(this.name)
				.build();
		}
	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class Response {

		private Long id;

		private String name;

		private String parent;

		private List<String> children = new ArrayList<>();

		public static Response toResponse(Category category) {
			return Response.builder()
				.id(category.getId())
				.name(category.getName())
				.parent(category.getParent().getName() == null ?
					"대분류" : category.getParent().getName())
				.children(category.getChildren().stream().map(Category::getName)
					.collect(Collectors.toList()))
				.build();
		}
	}


}
