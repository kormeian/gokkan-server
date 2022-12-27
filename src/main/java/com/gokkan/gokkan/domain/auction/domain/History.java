package com.gokkan.gokkan.domain.auction.domain;

import java.util.StringTokenizer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History {

	private Long memberId;
	private Long price;

	@Builder
	public History(Long memberId, Long price) {
		this.memberId = memberId;
		this.price = price;
	}

	public static History toHistory(String history) {
		StringTokenizer st = new StringTokenizer(history, "_");
		Long memberId = Long.parseLong(st.nextToken());
		Long price = Long.parseLong(st.nextToken());
		return History.builder()
			.memberId(memberId)
			.price(price)
			.build();
	}
}
