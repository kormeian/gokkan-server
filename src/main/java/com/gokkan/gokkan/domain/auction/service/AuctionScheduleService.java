package com.gokkan.gokkan.domain.auction.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.AuctionHistory;
import com.gokkan.gokkan.domain.auction.domain.History;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.repository.AuctionHistoryRepository;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionScheduleService {

	private final AuctionRepository auctionRepository;
	private final RedisTemplate<String, String> redisTemplate;

	@Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Seoul")
	@Transactional
	public void auctionSchedule() {
		log.info("경매 스케쥴러 실행");
		List<Auction> auctionList = auctionRepository.findAllByEndDateTimeLessThanEqualAndAuctionStatusEquals(
			LocalDateTime.now(), AuctionStatus.STARTED);
		if(auctionList.isEmpty()) {
			log.info("마감된 경매가 없습니다.");
			return;
		}
		for (Auction auction : auctionList) {
			auction.setAuctionStatus(AuctionStatus.WAIT_PAYMENT);
			auctionRepository.save(auction);
			deleteRedisHistory(auction.getId());
		}
	}

	private void deleteRedisHistory(Long auctionId) {
		redisTemplate.delete(String.valueOf(auctionId));
	}
}
