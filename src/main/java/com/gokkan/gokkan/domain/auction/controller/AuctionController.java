package com.gokkan.gokkan.domain.auction.controller;

import static com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.FilterListRequest;
import static com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ListResponse;

import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.AuctionOrderDetailAddress;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.AuctionOrderDetailItem;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.AuctionOrderDetailPaymentAmount;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ResponseAuctionHistory;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.ResponseAuctionInfo;
import com.gokkan.gokkan.domain.auction.domain.dto.AuctionDto.SimilarListRequest;
import com.gokkan.gokkan.domain.auction.service.AuctionService;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.global.security.oauth.token.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
		@Parameter(description = "카테고리 name", example = "/filter-list?category=의자")
			String category,
		@Parameter(description = "style name", example = "/filter-list?styles=Art Deco, Memphis")
			List<String> styles,
		@Parameter(description = "필터링 최저 가격", example = "/filter-list?minPrice=10")
			Long minPrice,
		@Parameter(description = "필터링 최대 가격", example = "/filter-list?maxPrice=10000")
			Long maxPrice,
		@Parameter(description = "정렬 순서", required = true, example = "/filter-list?sort=마감임박순 or /filter-list?sort=신규등록순")
			String sort,
		@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(auctionService.readList(FilterListRequest.builder()
				.category(category)
				.styles(styles)
				.minPrice(minPrice)
				.maxPrice(maxPrice)
				.sort(sort)
				.build(),
			pageable));
	}

	@GetMapping("/list/similar")
	@Operation(summary = "카테고리 유사 경매 list", description = "카테고리 유사 경매 list 5개 주요 정보")
	@ApiResponse(description = "카테고리 유사 경매 list 5개 주요 정보", content = @Content(schema = @Schema(implementation = ListResponse.class)))
	public ResponseEntity<List<ListResponse>> categorySimilarAuctionList(
		@Parameter(description = "카테고리 name", required = true)
			String category,
		@Parameter(description = "현재 경매 id -> 이 경매를 제외한 유사 경매 넘겨줌", required = true)
			Long auctionId) {
		return ResponseEntity.ok(auctionService.similarList(SimilarListRequest.builder()
			.category(category)
			.auctionId(auctionId)
			.build()));
	}

	@GetMapping("/list/member")
	@Operation(summary = "특정 유자가 올린 경매 list", description = "해당 유저가 올린 경매 list paging 처리해서 반환")
	@ApiResponse(description = "해당 유저가 올린 경매 list paging 처리해서 반환", content = @Content(schema = @Schema(implementation = ListResponse.class)))
	public ResponseEntity<?> auctionListMember(
		@Parameter(description = "카테고리 name", example = "/filter-list?category=의자")
			String category,
		@Parameter(description = "style name", example = "/filter-list?styles=Art Deco, Memphis")
			List<String> styles,
		@Parameter(description = "필터링 최저 가격", example = "/filter-list?minPrice=10")
			Long minPrice,
		@Parameter(description = "필터링 최대 가격", example = "/filter-list?maxPrice=10000")
			Long maxPrice,
		@Parameter(description = "정렬 순서", required = true, example = "/filter-list?sort=마감임박순 or /filter-list?sort=신규등록순")
			String sort,
		@Parameter(description = "찾고 싶은 유저의 닉네임", required = true, example = "/list/member?nickName=")
			String nickName,
		@ParameterObject Pageable pageable) {

		return ResponseEntity.ok(auctionService.readList(FilterListRequest.builder()
				.category(category)
				.styles(styles)
				.minPrice(minPrice)
				.maxPrice(maxPrice)
				.sort(sort)
				.memberNickName(nickName)
				.build(),
			pageable));
	}

	@GetMapping("/list/bid")
	@Operation(summary = "로그인된 유저의 낙찰, 응찰 된 경매 조회", description = "로그된 유저의 낙찰, 응찰 된 경매 조회")
	@ApiResponse(description = "경매 주요 정보를 page 처리해서 반환", content = @Content(schema = @Schema(implementation = ListResponse.class)))
	public ResponseEntity<Page<ListResponse>> myAuctionBidList(
		@Parameter(hidden = true) @CurrentMember Member member,
		@Parameter(name = "경매 상태", required = true, example = "/list/wait-payment?auctionStatus=경매중, /list/wait-payment?auctionStatus=마감, /list/wait-payment?auctionStatus=결재대기")
		@RequestParam String auctionStatus,
		@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(
			auctionService.myAuctionBidList(member, auctionStatus, pageable));
	}

	@GetMapping("/order/address")
	@Operation(summary = "경매 주문 상세 조회 (배송지)")
	@ApiResponse(description = "주문 상세 (배송지)", content = @Content(schema = @Schema(implementation = AuctionOrderDetailAddress.class)))
	public ResponseEntity<AuctionOrderDetailAddress> getAddressInfo(
		@Parameter(hidden = true) @CurrentMember Member member) {
		return ResponseEntity.ok(auctionService.getAddressInfo(member));
	}

	@GetMapping("/order/item")
	@Operation(summary = "경매 주문 상세 조회 (주문 상품)")
	@ApiResponse(description = "주문 상세 (주문 상품)", content = @Content(schema = @Schema(implementation = AuctionOrderDetailItem.class)))
	public ResponseEntity<AuctionOrderDetailItem> getItemInfo(
		@Parameter(description = "경매 아이디") @RequestParam Long auctionId,
		@Parameter(description = "상품 아이디") @RequestParam Long itemId) {
		return ResponseEntity.ok(auctionService.getItemInfo(auctionId, itemId));
	}

	@GetMapping("/order/pay")
	@Operation(summary = "경매 주문 상세 조회 (결제 금액)")
	@ApiResponse(description = "주문 상세 (결제 금액)", content = @Content(schema = @Schema(implementation = AuctionOrderDetailPaymentAmount.class)))
	public ResponseEntity<AuctionOrderDetailPaymentAmount> getPaymentAmount(
		@Parameter(description = "경매 아이디") @RequestParam Long auctionId) {
		return ResponseEntity.ok(auctionService.getPaymentAmount(auctionId));
	}
}
