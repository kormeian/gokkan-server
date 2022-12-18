package com.gokkan.gokkan.domain.member.domain.dto;

import com.gokkan.gokkan.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDto {

	@Getter
	@Schema(name = "유저 정보 수정 요청")
	public static class RequestUpdateDto {

		private String name;
		private String phoneNumber;
		private String profileImageUrl;
		private String address;
		private String cardNumber;
	}

	@Getter
	@Schema(name = "유저 정보")
	public static class ResponseDto {

		private final String name;
		private final String email;
		private final String phoneNumber;
		private final String profileImageUrl;
		private final String address;
		private final String cardNumber;

		@Builder
		public ResponseDto(String name, String email, String phoneNumber, String profileImageUrl,
			String address, String cardNumber) {
			this.name = name;
			this.email = email;
			this.phoneNumber = phoneNumber;
			this.profileImageUrl = profileImageUrl;
			this.address = address;
			this.cardNumber = cardNumber;
		}

		public static ResponseDto fromEntity(Member member) {
			return ResponseDto.builder()
				.name(member.getNickName())
				.email(member.getEmail())
				.phoneNumber(member.getPhoneNumber())
				.profileImageUrl(member.getProfileImageUrl())
				.address(member.getAddress())
				.cardNumber(member.getCardNumber())
				.build();
		}
	}

}
