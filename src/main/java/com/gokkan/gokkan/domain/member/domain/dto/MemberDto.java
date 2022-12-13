package com.gokkan.gokkan.domain.member.domain.dto;

import lombok.Getter;

@Getter
public class MemberDto {

	@Getter
	public static class RequestUpdateDto {

		private String name;
		private String email;
		private String phoneNumber;
		private String profileImageUrl;
		private String address;
		private String cardNumber;
	}

}
