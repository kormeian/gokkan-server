package com.gokkan.gokkan.domain.expertCareer.domain.dto;

import com.gokkan.gokkan.domain.expertCareer.domain.ExpertCareer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExpertCareerDto {

	@Getter
	@Schema(name = "전문가 커리어 생성 요청", description = "전문가 커리어 생성 요청")
	public static class RequestCreateExpertCareer {

		private final LocalDate startDate;
		private final LocalDate endDate;
		private final String companyName;
		private final String position;

		@Builder
		public RequestCreateExpertCareer(LocalDate startDate, LocalDate endDate, String companyName,
			String position) {
			this.startDate = startDate;
			this.endDate = endDate;
			this.companyName = companyName;
			this.position = position;
		}
	}

	@Getter
	@Schema(name = "전문가 커리어 수정 요청", description = "전문가 커리어 수정 요청")
	public static class RequestUpdateExpertCareer {
		private final Long expertCareerId;
		private final LocalDate startDate;
		private final LocalDate endDate;
		private final String companyName;
		private final String position;

		@Builder
		public RequestUpdateExpertCareer(Long expertCareerId, LocalDate startDate, LocalDate endDate,
			String companyName, String position) {
			this.expertCareerId = expertCareerId;
			this.startDate = startDate;
			this.endDate = endDate;
			this.companyName = companyName;
			this.position = position;
		}
	}

	@Getter
	@Schema(name = "전문가 커리어 응답", description = "전문가 커리어 응답")
	public static class ResponseGetExpertCareer {
		private final Long id;
		private final LocalDate startDate;
		private final LocalDate endDate;
		private final String companyName;
		private final String position;

		@Builder
		public ResponseGetExpertCareer(Long id, LocalDate startDate, LocalDate endDate, String companyName,
			String position) {
			this.id = id;
			this.startDate = startDate;
			this.endDate = endDate;
			this.companyName = companyName;
			this.position = position;
		}

		public static ResponseGetExpertCareer fromEntity(ExpertCareer expertCareer) {
			return ResponseGetExpertCareer.builder()
				.id(expertCareer.getId())
				.startDate(expertCareer.getStartDate())
				.endDate(expertCareer.getEndDate())
				.companyName(expertCareer.getCompanyName())
				.position(expertCareer.getPosition())
				.build();
		}
	}
}
