package com.gokkan.gokkan.domain.member.repository;

import com.gokkan.gokkan.domain.member.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
	UserRefreshToken findByUserId(String userId);
	UserRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}

