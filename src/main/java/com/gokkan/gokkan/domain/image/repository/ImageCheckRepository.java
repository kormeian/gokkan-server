package com.gokkan.gokkan.domain.image.repository;

import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageCheckRepository extends JpaRepository<ImageCheck, Long> {

}
