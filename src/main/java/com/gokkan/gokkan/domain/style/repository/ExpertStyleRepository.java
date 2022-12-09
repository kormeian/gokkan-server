package com.gokkan.gokkan.domain.style.repository;

import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertStyleRepository extends JpaRepository<ExpertStyle, Long> {

}
