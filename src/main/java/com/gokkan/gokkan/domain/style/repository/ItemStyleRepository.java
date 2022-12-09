package com.gokkan.gokkan.domain.style.repository;

import com.gokkan.gokkan.domain.style.domain.ItemStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemStyleRepository extends JpaRepository<ItemStyle, Long> {

}
