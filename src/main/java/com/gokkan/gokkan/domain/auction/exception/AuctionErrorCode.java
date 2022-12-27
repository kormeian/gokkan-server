package com.gokkan.gokkan.domain.auction.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuctionErrorCode implements ErrorCode {

	AUCTION_NOT_FOUND(HttpStatus.NOT_FOUND, "경매를 찾을 수 없습니다."),
	AUCTION_ALREADY_ENDED(HttpStatus.BAD_REQUEST, "경매가 이미 종료되었습니다."),
	AUCTION_PRICE_IS_LOWER_THAN_CURRENT_PRICE(HttpStatus.BAD_REQUEST, "입찰가가 현재 입찰가보다 낮습니다."),
	AUCTION_ANOTHER_USER_IS_BIDDING(HttpStatus.BAD_REQUEST, "다른 사용자가 경매에 입찰중입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
