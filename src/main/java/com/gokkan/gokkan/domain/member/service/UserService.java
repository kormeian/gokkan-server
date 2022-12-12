package com.gokkan.gokkan.domain.member.service;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final MemberRepository memberRepository;

	public Member getUser(String userId) {
		return memberRepository.findByUserId(userId);
	}
}

