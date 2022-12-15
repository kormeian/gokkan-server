package com.gokkan.gokkan.domain.heart.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HeartErrorCode {

	;
	private final HttpStatus httpStatus;
	private final String message;
}
