package com.gokkan.gokkan.domain.auction.domain.type;

import com.gokkan.gokkan.domain.member.exception.AuthErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {

	DESC("마감 시간 역순"),
	ASC("마감 시간 역순");

	private final String description;

	public static SortType getSortType(String description) {
		SortType[] values = SortType.values();
		for (SortType sortType : values) {
			if (sortType.getDescription().equals(description)) {
				return sortType;
			}
		}
		throw new RestApiException(AuthErrorCode.MISMATCH_SORT_TYPE);
	}
}
