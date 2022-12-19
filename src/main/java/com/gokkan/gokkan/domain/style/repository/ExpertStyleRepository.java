package com.gokkan.gokkan.domain.style.repository;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import com.gokkan.gokkan.domain.style.domain.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertStyleRepository extends JpaRepository<ExpertStyle, Long> {

	void deleteByExpertInfoAndStyle(ExpertInfo expertInfo, Style style);
}
