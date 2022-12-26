package com.gokkan.gokkan.global.webSocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MessageController {

	private final SimpMessageSendingOperations simpMessageSendingOperations;

	@MessageMapping("/topic/{auctionId}")
	//@SendTo("/topic/greeting")
	public void message(@PathVariable String auctionId, Long price) {

		String message = "auctionId : " + auctionId + " 경매가 " + price + "원으로 변경되었습니다.";
		//return "auctionId : " + auctionId + " price : " + price;
		simpMessageSendingOperations.convertAndSend("/topic/" + auctionId, message);
	}
}
