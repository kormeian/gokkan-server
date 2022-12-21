package com.gokkan.gokkan.domain.expertInfo.service;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByMemberId;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.RequestCreateExpertInfoByNickName;
import com.gokkan.gokkan.domain.expertInfo.domain.dto.ExpertInfoDto.ResponseGetExpertInfo;
import com.gokkan.gokkan.domain.expertInfo.exception.ExpertInfoErrorCode;
import com.gokkan.gokkan.domain.expertInfo.repository.ExpertInfoRepository;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpertInfoService {

	private final ExpertInfoRepository expertInfoRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void createExpertInfoByMemberId(
		RequestCreateExpertInfoByMemberId requestCreateExpertInfoByMemberId) {
		Member member = memberRepository.findById(requestCreateExpertInfoByMemberId.getMemberId())
			.orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
		if (existByMemberId(member.getId())) {
			throw new RestApiException(ExpertInfoErrorCode.ALREADY_EXIST_MEMBER);
		}
		expertInfoRepository.save(ExpertInfo.builder()
			.member(member)
			.name(member.getNickName())
			.info(requestCreateExpertInfoByMemberId.getInfo())
			.build());
	}

	@Transactional
	public void createExpertInfoByNickName(
		RequestCreateExpertInfoByNickName requestCreateExpertInfoByNickName) {
		Member member = memberRepository.findByNickName(
				requestCreateExpertInfoByNickName.getNickName())
			.orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
		if (existByMemberId(member.getId())) {
			throw new RestApiException(ExpertInfoErrorCode.ALREADY_EXIST_MEMBER);
		}
		expertInfoRepository.save(ExpertInfo.builder()
			.member(member)
			.name(member.getNickName())
			.info(requestCreateExpertInfoByNickName.getInfo())
			.build());
	}

	@Transactional
	public void updateExpertInfo(Member member, String info) {
		ExpertInfo expertInfo = findByMemberId(member.getId());
		expertInfo.updateInfo(info);
		expertInfoRepository.save(expertInfo);
	}

	@Transactional(readOnly = true)
	public ResponseGetExpertInfo getExpertInfo(Member member) {
		ExpertInfo expertInfo = findByMemberId(member.getId());
		return ResponseGetExpertInfo.fromEntity(expertInfo);
	}

	private boolean existByMemberId(Long memberId) {
		return expertInfoRepository.existsByMemberId(memberId);
	}

	private ExpertInfo findByMemberId(Long memberId) {
		return expertInfoRepository.findByMemberId(memberId)
			.orElseThrow(() -> new RestApiException(ExpertInfoErrorCode.EXPERT_INFO_NOT_FOUND));
	}

}
