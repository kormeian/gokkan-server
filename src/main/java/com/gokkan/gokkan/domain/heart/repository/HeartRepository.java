package com.gokkan.gokkan.domain.heart.repository;

import com.gokkan.gokkan.domain.heart.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

}
