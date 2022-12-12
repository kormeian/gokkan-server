package com.gokkan.gokkan.domain.member.repository;

import com.gokkan.gokkan.domain.member.domain.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

	MemberRefreshToken findByUserId(String userId);

	MemberRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}

