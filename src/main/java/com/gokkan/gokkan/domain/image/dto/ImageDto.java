package com.gokkan.gokkan.domain.image.dto;

import com.sun.istack.NotNull;
import java.util.List;
import lombok.*;

public class ImageDto {


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class CreateRequest {

		@NotNull
		Long itemId;
		List<String> urls;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	public static class UpdateRequest {

		@NotNull
		Long imageId;
		String url;
	}
}
