package com.gokkan.gokkan.domain.member.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {

	private final String accessToken;
	private final String refreshToken;

	@Builder
	public TokenDto(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
