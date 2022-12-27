package com.gokkan.gokkan.domain.item.dto;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.dto.ImageDto;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class ItemDto {


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	@Schema(name = "상품 생성 완료 request")
	public static class CreateRequest {

		@NotNull
		private Long itemId;
		@NotNull
		private String name;

		@NotNull
		private Long startPrice;

		@NotNull
		private String category;

		private List<ImageDto.UpdateRequest> imageItemUrls;

		private List<ImageDto.UpdateRequest> imageCheckUrls;

		private List<String> styles;

		@NotNull
		private Long width;
		@NotNull
		private Long depth;
		@NotNull
		private Long height;
		@NotNull
		private String material;

		@NotNull
		private String conditionGrade;
		@NotNull
		private String conditionDescription;
		@NotNull
		private String text;

		private String madeIn;
		private String designer;
		private String brand;
		private Integer productionYear;

		public Item toItem(Item item, Category category) {
			return Item.builder()
				.id(item.getId())
				.member(item.getMember())
				.name(this.name)
				.category(category)
				.startPrice(this.startPrice)
				.width(this.width)
				.depth(this.depth)
				.height(this.height)
				.material(this.material)
				.conditionGrade(this.conditionGrade)
				.conditionDescription(this.conditionDescription)
				.text(this.text)
				.madeIn(this.madeIn)
				.designer(this.designer)
				.brand(this.brand)
				.productionYear(this.productionYear)
				.state(item.getState())
				.created(item.getCreated())
				.updated(LocalDateTime.now())
				.imageItems(item.getImageItems())
				.imageChecks(item.getImageChecks())
				.styleItems(item.getStyleItems())
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	@Schema(name = "상품 수정 request")
	public static class UpdateRequest {

		@NotNull
		private Long itemId;
		private String name;

		private long startPrice;

		private String category;

		private List<ImageDto.UpdateRequest> imageItemUrls;

		private List<ImageDto.UpdateRequest> imageCheckUrls;

		private List<String> styles;

		private Long width;
		private Long depth;
		private Long height;
		private String material;

		private String conditionGrade;
		private String conditionDescription;
		private String text;

		private String madeIn;
		private String designer;
		private String brand;
		private Integer productionYear;


		public Item toItem(Item item, Category category) {
			return Item.builder()
				.id(item.getId())
				.member(item.getMember())
				.name(this.name)
				.category(category)
				.startPrice(this.startPrice)
				.width(this.width)
				.depth(this.depth)
				.height(this.height)
				.material(this.material)
				.conditionGrade(this.conditionGrade)
				.conditionDescription(this.conditionDescription)
				.text(this.text)
				.madeIn(this.madeIn)
				.designer(this.designer)
				.brand(this.brand)
				.productionYear(this.productionYear)
				.state(item.getState())
				.created(item.getCreated())
				.updated(LocalDateTime.now())
				.imageItems(item.getImageItems())
				.imageChecks(item.getImageChecks())
				.styleItems(item.getStyleItems())
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	@Schema(name = "상품 response")
	public static class Response {

		private Long id;

		private String name;
		private String thumbnail;

		private long startPrice;

		private String state;

		private Long width;
		private Long depth;
		private Long height;
		private String material;

		private String conditionGrade;
		private String conditionDescription;
		private String text;


		private String madeIn;
		private String designer;
		private String brand;
		private int productionYear;

		private String writer;
		private String category;
		private List<ImageDto.Response> imageItemUrls;
		private List<ImageDto.Response> imageCheckUrls;
		private List<String> styles;

		private LocalDateTime created;
		private LocalDateTime updated;

		public static Response toResponse(Item item) {
			List<ImageItem> imageItems = item.getImageItems();
			List<ImageCheck> imageChecks = item.getImageChecks();
			List<StyleItem> styleItems = item.getStyleItems();
			return Response.builder()
				.id(item.getId())
				.name(item.getName())
				.thumbnail(item.getThumbnail())
				.startPrice(item.getStartPrice())
				.state(item.getState().getDescription())
				.width(item.getWidth())
				.depth(item.getDepth())
				.height(item.getHeight())
				.material(item.getMaterial())
				.conditionGrade(item.getConditionGrade())
				.conditionDescription(item.getConditionDescription())
				.text(item.getText())
				.madeIn(item.getMadeIn())
				.designer(item.getDesigner())
				.brand(item.getBrand())
				.productionYear(item.getProductionYear())
				.created(item.getCreated())
				.updated(item.getUpdated())
				.writer(item.getMember().getNickName())
				.category(item.getCategory() == null ? null : item.getCategory().getName())
				.imageItemUrls(imageItems == null ? new ArrayList<>() :
					imageItems.stream()
						.map(x -> ImageDto.Response.builder()
							.id(x.getId())
							.url(x.getUrl())
							.build())
						.collect(Collectors.toList()))
				.imageCheckUrls(imageChecks == null ? new ArrayList<>() :
					imageChecks.stream()
						.map(x -> ImageDto.Response.builder()
							.id(x.getId())
							.url(x.getUrl())
							.build())
						.collect(Collectors.toList()))
				.styles(styleItems == null ? new ArrayList<>() :
					styleItems.stream().map(StyleItem::getName)
						.collect(Collectors.toList()))
				.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@Builder
	@Schema(name = "상품 List 주요 정보 response")
	public static class ListResponse {

		private Long id;

		private String name;

		private String thumbnail;

		private String state;

		private String writer;
		private String category;
		private List<String> styles;

		private LocalDateTime created;
		private LocalDateTime updated;

		public static ListResponse toResponse(Item item) {
			List<StyleItem> styleItems = item.getStyleItems();
			return ListResponse.builder()
				.id(item.getId())
				.name(item.getName())
				.thumbnail(item.getThumbnail())
				.state(item.getState().getDescription())
				.created(item.getCreated())
				.updated(item.getUpdated())
				.writer(item.getMember().getNickName())
				.category(item.getCategory() == null ? null : item.getCategory().getName())
				.styles(styleItems == null ? new ArrayList<>() :
					styleItems.stream().map(StyleItem::getName)
						.collect(Collectors.toList()))
				.build();
		}

		public static List<ListResponse> toResponse(List<Item> items) {
			return items.stream().map(ListResponse::toResponse).collect(Collectors.toList());
		}
	}
}
