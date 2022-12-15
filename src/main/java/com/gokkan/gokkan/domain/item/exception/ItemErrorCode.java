package com.gokkan.gokkan.domain.item.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ItemErrorCode implements ErrorCode {


	NOT_FOUND_ITEM(HttpStatus.BAD_REQUEST, "해당 상품이 존재하지 않습니다.");
	private final HttpStatus httpStatus;
	private final String message;
}
