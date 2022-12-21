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
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpertStyleService {

	private final ExpertStyleRepository expertStyleRepository;
	private final ExpertInfoRepository expertInfoRepository;
	private final StyleRepository styleRepository;

	@Transactional
	public void createStyleByStyleId(Long expertInfoId, Long styleId) {
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(
				ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findById(styleId).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.save(ExpertStyle.builder()
			.expertInfo(expertInfo)
			.style(style)
			.build());
	}

	@Transactional
	public void createStyleByStyleName(Long expertInfoId, String styleName) {
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(
				ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findByName(styleName).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.save(ExpertStyle.builder()
			.expertInfo(expertInfo)
			.style(style)
			.build());
	}

	@Transactional
	public void deleteExpertStyle(Long expertStyleId) {
		ExpertStyle expertStyle = expertStyleRepository.findById(expertStyleId)
			.orElseThrow(() -> new RestApiException(
				StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.delete(expertStyle);
	}

	@Transactional
	public void deleteStyleByStyleName(Long expertInfoId, String styleName) {
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(
				ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		Style style = styleRepository.findByName(styleName).orElseThrow(() -> new RestApiException(
			StyleErrorCode.NOT_FOUND_STYLE));
		ExpertStyle expertStyle = expertStyleRepository.findByExpertInfoAndStyle(expertInfo, style)
			.orElseThrow(() -> new RestApiException(
				StyleErrorCode.NOT_FOUND_STYLE));
		expertStyleRepository.delete(expertStyle);
	}

	@Transactional(readOnly = true)
	public List<String> getExpertStyles(Long expertInfoId) {
		ExpertInfo expertInfo = expertInfoRepository.findById(expertInfoId)
			.orElseThrow(() -> new RestApiException(
				ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
		//TODO: info에서 스타일 리스트 받기
		List<String> allByExpertInfo = expertStyleRepository.findAllByExpertInfo(expertInfo)
			.stream().map(
				ExpertStyle::getStyleName).collect(Collectors.toList());
		if (allByExpertInfo.isEmpty()) {
			throw new RestApiException(StyleErrorCode.NOT_FOUND_STYLE);
		}
		return allByExpertInfo;
	}
}
