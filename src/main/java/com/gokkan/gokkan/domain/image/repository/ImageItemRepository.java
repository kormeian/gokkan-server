package com.gokkan.gokkan.domain.image.repository;

import com.gokkan.gokkan.domain.image.domain.ImageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageItemRepository extends JpaRepository<ImageItem, Long> {

}
