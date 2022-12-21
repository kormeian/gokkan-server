package com.gokkan.gokkan.domain.expertComment.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.domain.expertComment.domain.ExpertComment;
import com.gokkan.gokkan.domain.expertComment.domain.dto.ExpertCommentDto.RequestCreateExpertComment;
import com.gokkan.gokkan.domain.expertComment.domain.dto.ExpertCommentDto.ResponseExpertComment;
import com.gokkan.gokkan.domain.expertComment.exception.ExpertCommentErrorCode;
import com.gokkan.gokkan.domain.expertComment.repository.ExpertCommentRepository;
import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpertCommentService {

	private final ExpertCommentRepository expertCommentRepository;
	private final ExpertInfoRepository expertInfoRepository;
	private final ItemRepository itemRepository;
	private final AuctionRepository auctionRepository;

	@Transactional
	public void createComment(Member member,
		RequestCreateExpertComment requestCreateExpertComment) {
		log.info("전문가 코멘트 생성");
		if (member == null) {
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		ExpertInfo expertInfo = expertInfoRepository.findByMember(member)
			.orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
		log.info("전문가 정보 아이디 : " + expertInfo.getId() + "아이템 아이디 : " + requestCreateExpertComment.getItemId());
		Item item = itemRepository.findById(requestCreateExpertComment.getItemId())
			.orElseThrow(() -> new RestApiException(ItemErrorCode.NOT_FOUND_ITEM));
		State requestState = requestCreateExpertComment.getStatus();
		if (!isStateAssessing(requestState)) {
			throw new RestApiException(ExpertCommentErrorCode.ITEM_STATE_NOT_ASSESSING);
		}
		ExpertComment expertComment = expertCommentRepository.save(
			RequestCreateExpertComment.toEntity(requestCreateExpertComment, expertInfo, item));
		if (isStateReturn(requestState)) {
			item.setState(State.RETURN);
			log.info("반려");
		} else if (isStateComplete(requestState)) {
			item.setState(State.COMPLETE);
			Auction auction = auctionRepository.save(createAuction(item, expertComment));
			log.info("감정완료, 경매 등록 완료 경매 아이디 : " + auction.getId());
		} else {
			throw new RestApiException(ExpertCommentErrorCode.NOT_FOUND_STATE);
		}
	}

	@Transactional(readOnly = true)
	public List<ResponseExpertComment> getExpertComments(Long expertInfoId) {
		log.info("전문가 코멘트 조회");
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		List<ExpertComment> expertComments = expertCommentRepository.findAllByExpertInfo(expertInfo);
		//TODO : DTO로 변환
		return new ArrayList<>();
	}

	private boolean isStateAssessing(State state) {
		return state.equals(State.ASSESSING);
	}

	private boolean isStateReturn(State state) {
		return state.equals(State.RETURN);
	}

	private boolean isStateComplete(State state) {
		return state.equals(State.COMPLETE);
	}

	private Auction createAuction(Item item, ExpertComment expertComment) {
		return Auction.builder()
			.startDateTime(LocalDateTime.now())
			.endDateTime(LocalDateTime.now().plusDays(7))
			.startPrice(item.getStartPrice())
			.currentPrice(item.getStartPrice())
			.auctionStatus(AuctionStatus.STARTED)
			.expertComment(expertComment)
			.build();
	}
}
