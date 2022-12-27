package com.gokkan.gokkan.domain.auction.controller;

import com.gokkan.gokkan.domain.auction.service.BidService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BidController {

	private final BidService bidService;

	@MessageMapping("/{auctionId}")
	public void message(
		@CurrentMember Member member,
		@PathVariable Long auctionId,
		Long price) {

		bidService.bidding(member, auctionId, price);
	}
}
