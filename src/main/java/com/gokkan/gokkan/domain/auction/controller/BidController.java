package com.gokkan.gokkan.domain.auction.controller;

import com.gokkan.gokkan.domain.auction.service.BidService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import com.gokkan.gokkan.global.webSocket.interceptor.StompChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BidController {

	private final BidService bidService;
	private final StompChannelInterceptor stompChannelInterceptor;

	@MessageMapping("/{auctionId}")
	public void message(
		@PathVariable Long auctionId,
		Long price) {
		Member member = stompChannelInterceptor.getMember();
		bidService.bidding(member, auctionId, price);
	}
}
