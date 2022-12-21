package com.gokkan.gokkan.domain.expertComment.domain.dto;

import com.gokkan.gokkan.domain.expertComment.domain.ExpertComment;
import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.type.State;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExpertCommentDto {

	@Getter
	public static class RequestCreateExpertComment {

		private final Long itemId;
		private final String comment;
		private final Long minPrice;
		private final Long maxPrice;
		private final State status;

		@Builder
		public RequestCreateExpertComment(Long itemId, String comment, Long minPrice, Long maxPrice,
			State status) {
			this.itemId = itemId;
			this.comment = comment;
			this.minPrice = minPrice;
			this.maxPrice = maxPrice;
			this.status = status;
		}

		public static ExpertComment toEntity(RequestCreateExpertComment requestCreateExpertComment,
			ExpertInfo expertInfo, Item item) {
			return ExpertComment.builder()
				.expertInfo(expertInfo)
				.item(item)
				.comment(requestCreateExpertComment.getComment())
				.minPrice(requestCreateExpertComment.getMinPrice())
				.maxPrice(requestCreateExpertComment.getMaxPrice())
				.build();
		}
	}

	@Getter
	public static class ResponseExpertComment {

		private final Long itemId;
		private final String itemName;
		private final String itemThumbnail;

		@Builder
		public ResponseExpertComment(Long itemId, String itemName, String itemThumbnail) {
			this.itemId = itemId;
			this.itemName = itemName;
			this.itemThumbnail = itemThumbnail;
		}
	}
}
