package com.gokkan.gokkan.domain.style.exception;

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
public class StyleErrorResponse {

	private StyleErrorCode errorCode;
	private String errorMessage;
}
