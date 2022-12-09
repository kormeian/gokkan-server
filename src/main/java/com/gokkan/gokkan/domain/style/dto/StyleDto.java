package com.gokkan.gokkan.domain.style.dto;

import com.gokkan.gokkan.domain.style.domain.Style;
import com.sun.istack.NotNull;
import lombok.*;


public class StyleDto {


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class CreateRequest {

		@NotNull
		private String name;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class UpdateRequest {

		@NotNull
		private Long id;

		@NotNull
		private String name;
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

		public static Response toResponse(Style style) {
			return Response.builder()
				.id(style.getId())
				.name(style.getName())
				.build();
		}
	}
}
