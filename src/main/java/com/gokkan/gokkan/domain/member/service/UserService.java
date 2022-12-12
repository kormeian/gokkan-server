package com.gokkan.gokkan.domain.member.service;

import com.gokkan.gokkan.domain.member.domain.User;
import com.gokkan.gokkan.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public User getUser(String userId) {
		return userRepository.findByUserId(userId);
	}
}

