package com.gokkan.gokkan.domain.member.repository;

import com.gokkan.gokkan.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByUserId(String userId);

	boolean existsByName(String nickName);
}

