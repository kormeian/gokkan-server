package com.gokkan.gokkan.domain.member.service;

import com.gokkan.gokkan.domain.member.exception.CertificationErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CertificationService {
	private final String IMP_KEY = "7685734080745845";
	private final String IMP_SECRET = "hBzf6iv8aPrgtzMK5XDcwGaeMgVXplSG5LlShz3qmY8wIVEtLWi44QWDYM69Ago1C2meKqbdq1k0FpVB";
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
			throw new RestApiException(CertificationErrorCode.CERTIFICATION_FAILED);
		}
		if (accessToken == null) {
			throw new RestApiException(CertificationErrorCode.CERTIFICATION_FAILED);
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
			throw new RestApiException(CertificationErrorCode.CERTIFICATION_NOT_FOUND);
		}
		String phone = "";
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			JSONObject searchResponse = (JSONObject) object.get("response");
			phone = (String) searchResponse.get("phone");
		} catch (ParseException e) {
			throw new RestApiException(CertificationErrorCode.CERTIFICATION_FAILED);
		}

		return phone;
	}

	public void getPaymentInfo(String imp_uid, String accessToken){
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
			throw new RestApiException(CertificationErrorCode.CERTIFICATION_NOT_FOUND);
		}
		try {
			JSONObject object = (JSONObject) jsonParser.parse(block);
			System.out.println(object);
		} catch (ParseException e) {
			throw new RestApiException(CertificationErrorCode.CERTIFICATION_FAILED);
		}

	}
}
