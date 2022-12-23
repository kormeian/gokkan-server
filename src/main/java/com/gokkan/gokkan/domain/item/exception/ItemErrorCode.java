package com.gokkan.gokkan.domain.item.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ItemErrorCode implements ErrorCode {


	NOT_FOUND_ITEM(HttpStatus.BAD_REQUEST, "해당 상품이 존재하지 않습니다."),
	CAN_NOT_UPDATE_STATE(HttpStatus.FORBIDDEN, "해당 상품은 수정할 수 없는 상태 입니다."),
	;
	private final HttpStatus httpStatus;
	private final String message;
}
