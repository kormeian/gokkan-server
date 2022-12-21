package com.gokkan.gokkan.domain.expertCareer.repository;

import com.gokkan.gokkan.domain.expertCareer.domain.ExpertCareer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertCareerRepository extends JpaRepository<ExpertCareer, Long> {


}
