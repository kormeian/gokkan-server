package com.gokkan.gokkan.domain.category.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryErrorResponse {

	private CategoryErrorCode errorCode;
	private String errorMessage;
}
