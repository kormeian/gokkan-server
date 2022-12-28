package com.gokkan.gokkan.domain.auction.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.AuctionHistory;
import com.gokkan.gokkan.domain.auction.domain.History;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.exception.AuctionErrorCode;
import com.gokkan.gokkan.domain.auction.repository.AuctionHistoryRepository;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidService {

	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final AuctionRepository auctionRepository;
	private final RedissonClient redissonClient;
	private final RedisTemplate<String, String> redisTemplate;
	private final AuctionHistoryRepository auctionHistoryRepository;


	public void bidding(Member member, Long auctionId, Long price) {
		if(member == null){
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RestApiException(
				AuctionErrorCode.AUCTION_NOT_FOUND));
		if (auction.getAuctionStatus() == AuctionStatus.ENDED) {
			throw new RestApiException(AuctionErrorCode.AUCTION_ALREADY_ENDED);
		}

		final String lockName = auctionId + ":lock";
		final RLock lock = redissonClient.getLock(lockName);
		try {
			if (!lock.tryLock(1, 3, TimeUnit.SECONDS)) {
				throw new RestApiException(AuctionErrorCode.AUCTION_ANOTHER_USER_IS_BIDDING);
			}

			List<History> history = getHistory(auctionId);
			Long currentPrice;
			if (history.isEmpty()) {
				currentPrice = auction.getCurrentPrice();
			} else {
				currentPrice = getCurrentPrice(history);
			}

			if (currentPrice >= price) {
				throw new RestApiException(
					AuctionErrorCode.AUCTION_PRICE_IS_LOWER_THAN_CURRENT_PRICE);
			}

			log.info("현재 진행중인 사람 : {} & 입찰가 : {}원", member.getId(), price);
			History currentHistory = setPrice(auctionId, member.getId(), price);
			auctionHistoryRepository.save(
				AuctionHistory.builder()
					.member(member)
					.auction(auction)
					.price(price)
					.bidDateTime(LocalDateTime.now())
					.build());
			history.add(0, currentHistory);
			auction.setCurrentPrice(price);
			auction.setMember(member);
			auctionRepository.save(auction);
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			for (History h : history) {
				jsonArray.add(h.getMemberId() + "_" + h.getPrice());
			}
			jsonObject.put("history", jsonArray);
			jsonObject.put("currentPrice", price);
			simpMessageSendingOperations.convertAndSend("/topic/auction/" + auctionId,
				jsonObject.toString());
		} catch (InterruptedException | JSONException e) {
			e.printStackTrace();
		} finally {
			if (lock != null && lock.isLocked()) {
				lock.unlock();
			}
		}


	}

	private List<History> getHistory(Long auctionId) {
		List<String> StringHistory = redisTemplate.opsForList()
			.range(String.valueOf(auctionId), 0, -1);
		if (StringHistory == null || StringHistory.isEmpty()) {
			return new ArrayList<>();
		}
		return StringHistory.stream()
			.map(History::toHistory)
			.collect(Collectors.toList());
	}

	private History setPrice(Long auctionId, Long memberId, Long currentPrice) {
		redisTemplate.opsForList()
			.leftPush(String.valueOf(auctionId), memberId + "_" + currentPrice);
		return History.builder()
			.memberId(memberId)
			.price(currentPrice)
			.build();
	}

	private Long getCurrentPrice(List<History> history) {
		return history.get(0).getPrice();
	}


}
