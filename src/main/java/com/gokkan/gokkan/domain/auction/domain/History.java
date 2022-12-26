package com.gokkan.gokkan.domain.auction.domain;

import java.util.StringTokenizer;
import lombok.Getter;

@Getter
public class History {

	private Long memberId;
	private Long price;

	public History toHistory(String history) {
		StringTokenizer st = new StringTokenizer(history, "_");
		this.memberId = Long.parseLong(st.nextToken());
		this.price = Long.parseLong(st.nextToken());
		return this;
	}
}
