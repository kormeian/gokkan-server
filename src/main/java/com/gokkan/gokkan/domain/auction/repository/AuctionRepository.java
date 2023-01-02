package com.gokkan.gokkan.domain.auction.repository;

import com.gokkan.gokkan.domain.auction.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionRepositoryCustom {


}
