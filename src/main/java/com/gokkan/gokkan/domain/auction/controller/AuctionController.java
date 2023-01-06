package com.gokkan.gokkan.domain.auction.controller;

import static com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import static com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ListResponse;

import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ResponseAuctionHistory;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ResponseAuctionInfo;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.SimilarListRequest;
import com.gokkan.gokkan.domain.auction.service.AuctionService;
import com.gokkan.gokkan.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction")
@Tag(name = "경매 컨트롤러", description = "경매 컨트롤러")
public class AuctionController {

	private final AuctionService auctionService;

	@GetMapping
	@Operation(summary = "경매 정보 현재가, 마감시간", description = "경매 정보 현재가, 마감시간")
	@ApiResponse(description = "경매 정보", content = @Content(schema = @Schema(implementation = ResponseAuctionInfo.class)))
	public ResponseEntity<ResponseAuctionInfo> getAuctionInfo(
		@Parameter(description = "경매 아이디") @RequestParam Long auctionId) {
		return ResponseEntity.ok(auctionService.getAuctionInfo(auctionId));
	}

	@GetMapping("/history")
	@Operation(summary = "경매 히스토리 조회(리스트임!!)", description = "경매 히스토리 조회(리스트임!!)")
	@ApiResponse(description = "경매 히스토리(리스트임!!)", content = @Content(schema = @Schema(implementation = ResponseAuctionHistory.class)))
	public ResponseEntity<List<ResponseAuctionHistory>> getAuctionHistory(
		@Parameter(description = "경매 아이디") @RequestParam Long auctionId) {
		return ResponseEntity.ok(auctionService.getAuctionHistory(auctionId));
	}

	@GetMapping("/filter-list")
	@Operation(summary = "경매 list filter", description = "경매 주요정보 포함한 list")
	@ApiResponse(description = "경매 주요 정보", content = @Content(schema = @Schema(implementation = ListResponse.class)))
	public ResponseEntity<?> auctionListFilter(
		@Parameter(description = "경매 list filter request", required = true, content = @Content(schema = @Schema(implementation = FilterListRequest.class)))
		@RequestBody FilterListRequest filterListRequest,
		@Parameter(description = "페이징처리 요구사항, sort 는 없이 보내면 됩니다. ex) &page=1&size=3", required = true)
		Pageable pageable) {
		return ResponseEntity.ok(auctionService.readList(filterListRequest, pageable));
	}

	@GetMapping("/list/similar")
	@Operation(summary = "카테고리 유사 경매 list", description = "카테고리 유사 경매 list 5개 주요 정보")
	@ApiResponse(description = "카테고리 유사 경매 list 5개 주요 정보", content = @Content(schema = @Schema(implementation = ListResponse.class)))
	public ResponseEntity<List<ListResponse>> categorySimilarAuctionList(
		@Parameter(description = "경매 list filter request", required = true, content = @Content(schema = @Schema(implementation = SimilarListRequest.class)))
		@RequestBody SimilarListRequest similarListRequest) {
		return ResponseEntity.ok(auctionService.similarList(similarListRequest));
	}

	@GetMapping("wait-payment")
	@Operation(summary = "결제 대기중인 경매 조회", description = "결제 대기중인 경매 조회")
	@ApiResponse(description = "경매 주요 정보", content = @Content(schema = @Schema(implementation = ListResponse.class)))
	public ResponseEntity<List<ListResponse>> waitPaymentAuctionList(
		@Parameter(hidden = true) Member member) {
		return ResponseEntity.ok(auctionService.getWaitPaymentAuctionList(member));
	}
}
