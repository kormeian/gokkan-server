package com.gokkan.gokkan.domain.auction.repository;

import static com.gokkan.gokkan.domain.auction.domain.QAuction.auction;
import static com.gokkan.gokkan.domain.expertComment.domain.QExpertComment.expertComment;
import static com.gokkan.gokkan.domain.item.domain.QItem.item;
import static com.gokkan.gokkan.domain.style.domain.QStyleItem.styleItem;

import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ListResponse;
import com.gokkan.gokkan.domain.auction.domain.dto.QAuctionDto_ListResponse;
import com.gokkan.gokkan.domain.auction.domain.type.SortType;
import com.gokkan.gokkan.domain.category.domain.Category;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
			.where(eqCategory(filterListRequest.getCategory()),
				eqStyle(filterListRequest.getStyles()))
			.groupBy(auction)
			.orderBy(
				filterListRequest.getSort().equals(SortType.DESC) ?
					auction.endDateTime.desc() : auction.endDateTime.asc())
			.fetch();
	}


	private BooleanBuilder eqCategory(Category category) {
		if (category == null) {
			return null;
		}

		return new BooleanBuilder().or(item.category.eq(category));
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