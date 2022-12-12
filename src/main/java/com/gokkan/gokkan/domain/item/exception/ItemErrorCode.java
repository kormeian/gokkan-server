package com.gokkan.gokkan.domain.item.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemErrorCode {


	NOT_FOUND_ITEM("해당 상품이 존재하지 않습니다.");
	private final String description;
}
