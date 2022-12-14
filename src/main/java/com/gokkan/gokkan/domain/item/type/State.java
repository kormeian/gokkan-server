package com.gokkan.gokkan.domain.item.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State {
	CLOSED("마감"),
	RETURN("반려"),
	ASSESSING("감정중"),
	AUCTION("경매중"),
	;

	private final String description;
}
