package com.gokkan.gokkan.domain.auction.controller;

import com.gokkan.gokkan.domain.auction.service.BidService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.exception.response.ErrorResponse;
import com.gokkan.gokkan.global.webSocket.interceptor.StompChannelInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BidController {

	private final BidService bidService;
	private final StompChannelInterceptor stompChannelInterceptor;

	@MessageMapping("/{auctionId}")
	public void message(
		@DestinationVariable Long auctionId,
		Long price) {
		Member member = stompChannelInterceptor.getMember();
		log.info("멤버 아이디 : " + member.getId() + "가 입찰을 시작합니다. 경매 아이디 : " + auctionId + " 입찰 가격 : " + price);
		bidService.bidding(member, auctionId, price);
	}

}
