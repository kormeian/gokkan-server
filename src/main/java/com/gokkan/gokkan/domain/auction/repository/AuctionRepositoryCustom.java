package com.gokkan.gokkan.domain.auction.repository;

import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepositoryCustom {

	List<AuctionDto.ListResponse> searchAllFilter(FilterListRequest filterListRequest);
}
