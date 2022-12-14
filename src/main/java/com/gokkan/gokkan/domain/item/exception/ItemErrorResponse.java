package com.gokkan.gokkan.domain.item.exception;

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
public class ItemErrorResponse {

	private ItemErrorCode errorCode;
	private String errorMessage;
}
