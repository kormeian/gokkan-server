package com.gokkan.gokkan.domain.image.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageErrorResponse {

	private ImageErrorCode errorCode;
	private String errorMessage;
}
