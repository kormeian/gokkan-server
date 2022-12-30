package com.gokkan.gokkan.domain.auction.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class AuctionDto {

	@Getter
	@Builder
	@Schema(name = "경매 정보", description = "경매 정보")
	public static class ResponseAuctionInfo {

		private LocalDateTime auctionEndDateTime;
		private Long currentPrice;
	}

	@Getter
	@Builder
	@Schema(name = "경매 히스토리", description = "경매 히스토리")
	public static class ResponseAuctionHistory {

		private String memberId;
		private Long price;
		private LocalDateTime bidTime;

		public static ResponseAuctionHistory of(String memberId, Long price,
			LocalDateTime bidTime) {
			return ResponseAuctionHistory.builder()
				.memberId(memberId)
				.price(price)
				.bidTime(bidTime)
				.build();
		}
	}

}
