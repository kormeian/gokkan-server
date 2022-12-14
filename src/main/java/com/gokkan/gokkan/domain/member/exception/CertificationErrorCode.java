package com.gokkan.gokkan.domain.member.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CertificationErrorCode implements ErrorCode {

	CERTIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "Certification failed"),
	CERTIFICATION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증 정보가 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
