package com.gokkan.gokkan.domain.style.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StyleErrorCode {

	NOT_FOUND_STYLE("존재하지 않는 스타일 입니다."),
	DUPLICATE_STYLE("중복된 스타일 입니다."),
	;

	private final String description;
}
