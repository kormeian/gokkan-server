package com.gokkan.gokkan.domain.style.repository;

import com.gokkan.gokkan.domain.style.domain.StyleExpert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleExpertRepository extends JpaRepository<StyleExpert, Long> {

}
