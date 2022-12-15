package com.gokkan.gokkan.domain.item.dto;

import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.type.State;
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
	@Schema(name = "상품 생성 request")
	public static class CreateRequest {

		@NotNull
		private String name;

		@NotNull
		private long startPrice;

		@NotNull
		private String category;

		@NotNull
		private Long length;
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

		@NotNull
		private String madeIn;
		@NotNull
		private String designer;
		@NotNull
		private String brand;
		@NotNull
		private int productionYear;

		public Item toItem() {
			return Item.builder()
				.name(this.name)
				.startPrice(this.startPrice)
				.length(this.length)
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
				.state(State.ASSESSING)
				.assessed(false)
				.created(LocalDateTime.now())
				.updated(LocalDateTime.now())
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
		@NotNull
		private String name;

		@NotNull
		private long startPrice;

		@NotNull
		private String category;

		@NotNull
		private Long length;
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

		@NotNull
		private String madeIn;
		@NotNull
		private String designer;
		@NotNull
		private String brand;
		@NotNull
		private int productionYear;

		public Item toItem(Item item) {
			return Item.builder()
				.id(item.getId())
				.name(this.name)
				.startPrice(this.startPrice)
				.length(this.length)
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
				.assessed(item.isAssessed())
				.created(item.getCreated())
				.updated(LocalDateTime.now())
				.imageItems(item.getImageItems())
				.imageChecks(item.getImageChecks())
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

		private long startPrice;

		private String state;

		private Long length;
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
		private boolean assessed;

		private List<String> imageItemUrls = new ArrayList<>();
		private List<String> imageCheckUrls = new ArrayList<>();

		private LocalDateTime created;
		private LocalDateTime updated;

		public static Response toResponse(Item item) {
			List<ImageItem> imageItems = item.getImageItems();
			List<ImageCheck> imageChecks = item.getImageChecks();
			return Response.builder()
				.id(item.getId())
				.name(item.getName())
				.startPrice(item.getStartPrice())
				.state(item.getState().getDescription())
				.length(item.getLength())
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
				.imageItemUrls(
					imageItems == null ? new ArrayList<>() :
						item.getImageItems().stream().map(ImageItem::getUrl)
							.collect(Collectors.toList()))
				.imageCheckUrls(imageChecks == null ? new ArrayList<>() :
					imageChecks.stream().map(ImageCheck::getUrl)
						.collect(Collectors.toList()))
				.build();

		}
	}
}
