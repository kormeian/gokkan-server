package com.gokkan.gokkan.domain.auction.repository;

import static com.gokkan.gokkan.domain.auction.domain.QAuction.auction;
import static com.gokkan.gokkan.domain.expertComment.domain.QExpertComment.expertComment;
import static com.gokkan.gokkan.domain.item.domain.QItem.item;
import static com.gokkan.gokkan.domain.style.domain.QStyleItem.styleItem;

import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ListResponse;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.SimilarListRequest;
import com.gokkan.gokkan.domain.auction.domain.dto.QAuctionDto_ListResponse;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.domain.type.SortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ListResponse> searchAllFilter(FilterListRequest filterListRequest) {
		return jpaQueryFactory
			.select(new QAuctionDto_ListResponse(
					auction.id,
					item.id,
					item.name,
					item.thumbnail,
					auction.currentPrice,
					item.member.name,
					auction.endDateTime
				)
			).from(auction)
			.innerJoin(auction.expertComment, expertComment)
			.innerJoin(expertComment.item, item)
			.innerJoin(item.styleItems, styleItem)
			.where(
				auction.auctionStatus.eq(AuctionStatus.STARTED),
				eqCategory(filterListRequest.getCategory()),
				eqStyle(filterListRequest.getStyles()),
				auction.endDateTime.after(LocalDateTime.now())
			)
			.groupBy(auction)
			.orderBy(
				SortType.getSortType(filterListRequest.getSort()).equals(SortType.DESC) ?
					auction.endDateTime.desc() : auction.endDateTime.asc())
			.fetch();
	}

	@Override
	public List<ListResponse> searchAllSimilar(SimilarListRequest similarListRequest) {
		return jpaQueryFactory
			.select(new QAuctionDto_ListResponse(
					auction.id,
					item.id,
					item.name,
					item.thumbnail,
					auction.currentPrice,
					item.member.name,
					auction.endDateTime
				)
			).from(auction)
			.innerJoin(auction.expertComment, expertComment)
			.innerJoin(expertComment.item, item)
			.where(
				auction.auctionStatus.eq(AuctionStatus.STARTED),
				eqCategory(similarListRequest.getCategory()),
				auction.endDateTime.after(LocalDateTime.now()),
				auction.id.eq(similarListRequest.getAuctionId()).not()
			)
			.groupBy(auction)
			.orderBy(auction.endDateTime.desc())
			.limit(5)
			.fetch();
	}


	private BooleanBuilder eqCategory(String category) {
		if (category == null) {
			return null;
		}
		return new BooleanBuilder().or(item.category.name.eq(category));
	}


	private BooleanBuilder eqStyle(List<String> names) {

		if (names == null || names.size() == 0) {
			return null;
		}

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		for (String name : names) {
			booleanBuilder.or(styleItem.name.eq(name));
		}

		return booleanBuilder;
	}
}