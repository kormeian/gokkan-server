package com.gokkan.gokkan.domain.style.dto;

import com.gokkan.gokkan.domain.style.domain.Style;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


public class StyleDto {


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	@Schema(name = "스타일 생성 request")
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
	@Schema(name = "스타일 수정 request")
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
	@Schema(name = "스타일 response")
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
