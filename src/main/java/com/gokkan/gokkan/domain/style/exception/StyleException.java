package com.gokkan.gokkan.domain.style.exception;

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
public class StyleException extends RuntimeException {

	private StyleErrorCode errorCode;
	private String errorMessage;

	public StyleException(StyleErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getDescription();
	}

}
