package com.gokkan.gokkan.domain.auction.repository;

import static com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.*;

import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepositoryCustom {

	List<ListResponse> searchAllFilter(FilterListRequest filterListRequest);

	List<ListResponse> searchAllSimilar(SimilarListRequest similarListRequest);
}
