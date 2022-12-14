package com.gokkan.gokkan.domain.image.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ImageException extends RuntimeException {

	private ImageErrorCode errorCode;
	private String errorMessage;

	public ImageException(ImageErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getDescription();
	}

}
