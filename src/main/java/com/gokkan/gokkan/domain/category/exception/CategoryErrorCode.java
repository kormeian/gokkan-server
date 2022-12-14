package com.gokkan.gokkan.domain.category.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode {
	NOT_FOUND_PARENT_CATEGORY("상위 카테고리가 존재하지 않습니다."),
	NOT_FOUND_CATEGORY("존재하지 않는 카테고리 입니다."),
	DUPLICATED_CATEGORY("이미 존재하는 카테고리 입니다."),
	CAN_NOT_SAME_PARENT_NAME("부모카테고리와 같은 이름을 사용 할 수 없습니다.");

	private final String description;
}
