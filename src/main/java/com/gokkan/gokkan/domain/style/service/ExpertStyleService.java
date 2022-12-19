package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.repository.ExpertStyleRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpertStyleService {

	private final ExpertStyleRepository expertStyleRepository;
	private final ExpertInfoRepository expertInfoRepository;
	private final StyleRepository styleRepository;

	public void createStyleByStyleId(Long expertInfoId, Long styleId) {
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId).orElseThrow(() -> new RestApiException(
			ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findById(styleId).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.save(ExpertStyle.builder()
			.expertInfo(expertInfo)
			.style(style)
			.build());
	}

	public void createStyleByStyleName(Long expertInfoId, String styleName) {
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId).orElseThrow(() -> new RestApiException(
			ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findByName(styleName).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.save(ExpertStyle.builder()
			.expertInfo(expertInfo)
			.style(style)
			.build());
	}

	public void deleteExpertStyle(Long expertStyleId) {
		ExpertStyle expertStyle = expertStyleRepository.findById(expertStyleId).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.delete(expertStyle);
	}

	public void deleteStyleByStyleName(Long expertInfoId, String styleName) {
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId).orElseThrow(() -> new RestApiException(
			ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findByName(styleName).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.deleteByExpertInfoAndStyle(expertInfo, style);
	}
}
