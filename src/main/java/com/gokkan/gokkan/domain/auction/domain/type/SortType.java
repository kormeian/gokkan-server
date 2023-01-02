package com.gokkan.gokkan.domain.auction.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {

	DESC("역순"),
	ASC("정순");

	private final String sortSql;
}
