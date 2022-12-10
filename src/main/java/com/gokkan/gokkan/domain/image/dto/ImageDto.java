package com.gokkan.gokkan.domain.image.dto;

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

		Long imageId;
		String url;
	}
}
