package com.gokkan.gokkan.domain.auction.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
//class BidServiceTest {
//
//	@Autowired
//	private AuctionRepository auctionRepository;
//	@Autowired
//	private MemberRepository memberRepository;
//	@Autowired
//	private BidService bidService;
//
//
//	@Test
//	void test() {
//		//given
//		Auction auction = auctionRepository.save(Auction.builder()
//			.startDateTime(LocalDateTime.now())
//			.endDateTime(LocalDateTime.now().plusDays(7))
//			.startPrice(1000L)
//			.currentPrice(1000L)
//			.auctionStatus(AuctionStatus.STARTED)
//			.build());
//		Long auctionId = auction.getId();
//		Member member = memberRepository.save(Member.builder()
//			.name("name")
//				.userId("userId")
//				.role(Role.USER)
//				.providerType(ProviderType.KAKAO)
//				.email("email")
//				.profileImageUrl("profileImageUrl")
//			.build());
//
//		//when
//		bidService.bidding(member, auctionId, 1100L);
//
//		//then
//	}
//
//}