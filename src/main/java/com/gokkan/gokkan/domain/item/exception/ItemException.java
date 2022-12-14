package com.gokkan.gokkan.domain.item.exception;

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
public class ItemException extends RuntimeException {

	private ItemErrorCode errorCode;
	private String errorMessage;

	public ItemException(ItemErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getDescription();
	}

}
