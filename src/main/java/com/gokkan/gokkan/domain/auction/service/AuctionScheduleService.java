package com.gokkan.gokkan.domain.auction.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionScheduleService {

	private final AuctionRepository auctionRepository;

	@Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Seoul")
	public void auctionSchedule() {
		log.info("경매 스케쥴러 실행");
		List<Auction> auctionList = auctionRepository.findAllByEndDateTimeLessThanEqualAndAuctionStatusEquals(
			LocalDateTime.now(), AuctionStatus.STARTED);
		for (Auction auction : auctionList) {
			auction.setAuctionStatus(AuctionStatus.WAIT_PAYMENT);
			auctionRepository.save(auction);
		}
	}
}
