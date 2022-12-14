package com.gokkan.gokkan.domain.member.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "Authentication failed"),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token expired"),
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Token not found"),
	REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh token not found"),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token expired"),
	ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Access Token invalid"),
	REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Refresh Token invalid");

	private final HttpStatus httpStatus;
	private final String message;
}
