package com.gokkan.gokkan.domain.category.exception;

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
public class CategoryException extends RuntimeException{

	private CategoryErrorCode errorCode;
	private String errorMessage;

	public CategoryException(CategoryErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getDescription();
	}

}
