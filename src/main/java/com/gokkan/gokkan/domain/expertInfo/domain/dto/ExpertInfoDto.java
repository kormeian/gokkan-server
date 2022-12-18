package com.gokkan.gokkan.domain.expertInfo.domain.dto;


import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class ExpertInfoDto {

	@Getter
	public static class RequestCreateExpertInfoByMemberId {

		@NotNull
		private Long memberId;
		@NotNull
		private String name;
		private String info;

		@Builder
		public RequestCreateExpertInfoByMemberId(Long memberId, String name, String info) {
			this.memberId = memberId;
			this.name = name;
			this.info = info;
		}
	}

	@Getter
	public static class RequestCreateExpertInfoByNickName {

		@NotNull
		private String nickName;
		@NotNull
		private String name;
		private String info;

		@Builder
		public RequestCreateExpertInfoByNickName(String nickName, String name, String info) {
			this.nickName = nickName;
			this.name = name;
			this.info = info;
		}
	}

	@Getter
	public static class ResponseGetExpertInfo {

		private String name;
		private String info;

		@Builder
		public ResponseGetExpertInfo(String name, String info) {
			this.name = name;
			this.info = info;
		}

		public static ResponseGetExpertInfo fromEntity(ExpertInfo expertInfo) {
			return new ResponseGetExpertInfo(expertInfo.getName(), expertInfo.getInfo());
		}
	}

}
