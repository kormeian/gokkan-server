package com.gokkan.gokkan.domain.category.dto;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

public class CategoryDto {

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	@Schema(name = "카테고리 생성시 필요한 request")
	public static class CreateRequest {

		@NonNull
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
	@Schema(name = "카테고리 수정시 필요한 request")
	public static class UpdateRequest {

		@NotNull
		private Long id;

		@NonNull
		private String name;

		@NonNull
		private String parent;

	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	@Schema(name = "카테고리 response")
	public static class Response {

		private Long id;

		private String name;

		private String parent;

		private List<Response> children;

		public static Response toResponse(Category category) {
			return Response.builder()
				.id(category.getId())
				.name(category.getName())
				.parent(category.getParent() == null ?
					"root" : category.getParent().getName())
				.children(category.getChildren() == null ? new ArrayList<>() :
					category.getChildren().stream().map(Response::toResponse)
						.collect(Collectors.toList()))
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class ResponseForItem {

		private String name;

		private List<ResponseForItem> children;

		public static ResponseForItem toResponseForItem(Category category,
			List<ResponseForItem> child) {
			ResponseForItem response = ResponseForItem.builder()
				.name(category.getName())
				.children(new ArrayList<>())
				.build();

			if (child != null) {
				response.children.addAll(child);
			}
			return response;
		}

		public static ResponseForItem getResponseForItem(Category category,
			List<ResponseForItem> child) {
			Category parent = category.getParent();
			if (parent == null || parent.getName().equals("root")) {
				return toResponseForItem(category, child);
			}

			return getResponseForItem(parent,
				new ArrayList<>(List.of(toResponseForItem(category, child))));
		}

	}
}
