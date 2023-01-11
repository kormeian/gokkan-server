package com.gokkan.gokkan.global.iamport.service;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import com.gokkan.gokkan.domain.auction.domain.type.AuctionStatus;
import com.gokkan.gokkan.domain.auction.exception.AuctionErrorCode;
import com.gokkan.gokkan.domain.auction.repository.AuctionRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.iamport.exception.IamportErrorCode;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class IamportService {

	private final AuctionRepository auctionRepository;
	@Value("${iamport.imp-key}")
	private String IMP_KEY;
	@Value("${iamport.imp-secret}")
	private String IMP_SECRET;

	public String getAccessToken() {

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("imp_key", IMP_KEY);
		formData.add("imp_secret", IMP_SECRET);
		WebClient webClient = WebClient.create("https://api.iamport.kr/users/getToken");
		String block = webClient.post()
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.bodyValue(formData)
			.retrieve()
			.bodyToMono(String.class)
			.block();
		JSONParser jsonParser = new JSONParser();
		String accessToken = "";
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			accessToken = (String) searchResponse.get("access_token");
		} catch (ParseException e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}
		if (accessToken == null) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}

		return accessToken;
	}

	public String getPhoneNumber(String imp_uid, String accessToken) {
		WebClient webClient = WebClient.create("https://api.iamport.kr/certifications/" + imp_uid);
		JSONParser jsonParser = new JSONParser();
		String block = "";
		try {
			block = webClient.get()
				.header("Authorization", accessToken)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch (Exception e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_NOT_FOUND);
		}
		String phone = "";
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			phone = (String) searchResponse.get("phone");
		} catch (ParseException e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}

		return phone;
	}

	@Transactional
	public void paymentVerify(Long auctionId, String imp_uid, String accessToken) {
		WebClient webClient = WebClient.create("https://api.iamport.kr/payments/" + imp_uid);
		JSONParser jsonParser = new JSONParser();
		String block = "";
		try {
			block = webClient.get()
				.header("Authorization", accessToken)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch (Exception e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_NOT_FOUND);
		}
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RestApiException(
				AuctionErrorCode.AUCTION_NOT_FOUND));
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			Long amount = (Long) searchResponse.get("amount");
			String status = (String) searchResponse.get("status");
			if (status.equals("failed")) {
				throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
			} else if (!auction.getCurrentPrice().equals(amount)) {
				paymentCancel(imp_uid, accessToken);
				throw new RestApiException(IamportErrorCode.IAMPORT_NOT_MATCH_AMOUNT);
			}
			auction.setAuctionStatus(AuctionStatus.ENDED);
		} catch (ParseException e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}
		auctionRepository.save(auction);
	}

	private void paymentCancel(String imp_uid, String accessToken) {
		WebClient webClient = WebClient.create("https://api.iamport.kr/payments/cancel/" + imp_uid);
		JSONParser jsonParser = new JSONParser();
		String block = "";
		try {
			block = webClient.get()
				.header("Authorization", accessToken)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch (Exception e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_NOT_FOUND);
		}
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			String status = (String) searchResponse.get("status");
			if (status.equals("failed") || status.equals("ready") || status.equals("paid")) {
				throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
			}

		} catch (ParseException e) {
			throw new RestApiException(IamportErrorCode.IAMPORT_FAILED);
		}
	}
}
