package com.gokkan.gokkan.domain.auction.domain.dto;

import com.gokkan.gokkan.domain.auction.domain.type.SortType;
import com.gokkan.gokkan.domain.category.domain.Category;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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


	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(name = "경매 List filter api request")
	public static class FilterListRequest {

		private Category category;
		private List<String> styles;
		private SortType sort;

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Builder
	@Schema(name = "경매 List filter api 주요 정보 response")
	public static class ListResponse {

		private Long id;
		private Long itemId;

		private String name;

		private String thumbnail;

		private Long currentPrice;

		private String writer;

		private LocalDateTime auctionEndDateTime;

		@QueryProjection
		public ListResponse(
			Long id,
			Long itemId,
			String name,
			String thumbnail,
			Long currentPrice,
			String writer,
			LocalDateTime auctionEndDateTime
		) {
			this.id = id;
			this.itemId = itemId;
			this.name = name;
			this.currentPrice = currentPrice;
			this.thumbnail = thumbnail;
			this.writer = writer;
			this.auctionEndDateTime = auctionEndDateTime;
		}

		@Builder
		public ListResponse(
			Long id,
			Long itemId,
			String name,
			String thumbnail,
			Long currentPrice,
			String writer
		) {
			this.id = id;
			this.itemId = itemId;
			this.name = name;
			this.currentPrice = currentPrice;
			this.thumbnail = thumbnail;
			this.writer = writer;
		}
	}

}
