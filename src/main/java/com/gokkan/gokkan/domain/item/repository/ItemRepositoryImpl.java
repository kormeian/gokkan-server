package com.gokkan.gokkan.domain.item.repository;

import static com.gokkan.gokkan.domain.expertInfo.domain.QExpertInfo.expertInfo;
import static com.gokkan.gokkan.domain.item.domain.QItem.item;
import static com.gokkan.gokkan.domain.style.domain.QExpertStyle.expertStyle;
import static com.gokkan.gokkan.domain.style.domain.QStyleItem.styleItem;

import com.gokkan.gokkan.domain.item.dto.ItemDto.ListResponse;
import com.gokkan.gokkan.domain.item.dto.QItemDto_ListResponse;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<ListResponse> searchAllItemForExport(Member member, Pageable pageable) {
		// 전문가 스타일 불러오기
		List<String> expertStyleNames = jpaQueryFactory
			.select(expertStyle.styleName).from(expertStyle)
			.innerJoin(expertStyle.expertInfo, expertInfo)
			.where(expertInfo.member.eq(member))
			.fetch();

		// 1. 스타일 아이템에서 스타일 포함된 모든 스타일 아이템 불러오기
		// 2. 불러온 스타일 아이템 아이템 id 로 묶기
		// 3. 가져온 id 묶음으로 상품 list 불러오기
		// 전문가 스탕일로 필터링 된 item list 불러오기

		List<ListResponse> content = jpaQueryFactory
			.select(
				new QItemDto_ListResponse(item.id,
					item.name,
					item.thumbnail,
					item.member.nickName,
					item.state,
					item.created,
					item.updated))
			.from(item)
			.innerJoin(item.styleItems, styleItem)
			.where(eqStyle(expertStyleNames), item.state.eq(State.ASSESSING))
			.groupBy(item)
			.orderBy(item.created.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory.select(item.countDistinct())
			.from(item)
			.innerJoin(item.styleItems, styleItem)
			.where(eqStyle(expertStyleNames), item.state.eq(State.ASSESSING));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<ListResponse> searchAllMyItem(List<State> states, Member member,
		Pageable pageable) {
		List<ListResponse> content = jpaQueryFactory.select(new QItemDto_ListResponse(item.id,
				item.name,
				item.thumbnail,
				item.member.nickName,
				item.state,
				item.created,
				item.updated))
			.from(item)
			.where(
				item.member.eq(member),
				(eqState(states))
			)
			.orderBy(item.created.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory.select(item.countDistinct())
			.from(item)
			.where(
				item.member.eq(member),
				(eqState(states))
			);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private BooleanBuilder eqState(List<State> states) {

		if (states == null || states.size() == 0) {
			return null;
		}

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		for (State state : states) {
			booleanBuilder.or(item.state.eq(state));
		}

		return booleanBuilder;
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