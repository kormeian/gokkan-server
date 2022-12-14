package com.gokkan.gokkan.domain.item.domain;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.item.type.State;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100)
	private String name;

	@Column(length = 10)
	private long startPrice;

	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private State state;

	@Column(length = 5)
	private Long length;
	@Column(length = 5)
	private Long width;
	@Column(length = 5)
	private Long depth;
	@Column(length = 5)
	private Long height;
	private String material;

	@Column(length = 3)
	private String conditionGrade;
	@Column(length = 2000)
	private String conditionDescription;
	@Column(length = 2000)
	private String text;


	@Column(length = 20)
	private String madeIn;
	@Column(length = 20)
	private String designer;
	@Column(length = 20)
	private String brand;
	@Column(length = 4)
	private int productionYear;
	private boolean assessed;

	private LocalDateTime created;
	private LocalDateTime updated;


	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@BatchSize(size = 11)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.REMOVE)
	private List<ImageCheck> imageChecks = new ArrayList<>();

	@BatchSize(size = 11)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.REMOVE)
	private List<ImageItem> imageItems = new ArrayList<>();


	public void addImageItems(List<ImageItem> imageItems) {
		for (ImageItem imageItem : imageItems) {
			imageItem.setItem(this);
		}
		this.setImageItems(imageItems);
	}

	public void addImageChecks(List<ImageCheck> imageChecks) {
		for (ImageCheck imageCheck : imageChecks) {
			imageCheck.setItem(this);
		}
		this.setImageChecks(imageChecks);
	}
}
