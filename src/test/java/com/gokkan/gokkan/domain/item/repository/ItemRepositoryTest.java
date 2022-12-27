package com.gokkan.gokkan.domain.item.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.repository.CategoryRepository;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import com.gokkan.gokkan.domain.image.repository.ImageItemRepository;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import com.gokkan.gokkan.global.querydsl.config.QueryDslConfig;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
class ItemRepositoryTest {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ImageItemRepository imageItemRepository;
	@Autowired
	private ImageCheckRepository imageCheckRepository;
	@Autowired
	private StyleItemRepository styleItemRepository;
	@Autowired
	private StyleRepository styleRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("01_00. searchAllMyItem success size 2")
	@Test
	public void test_01_00() {
		//given
		Style styleSaved1 = styleRepository.save(Style.builder().name(style1).build());
		Style styleSaved2 = styleRepository.save(Style.builder().name(style2).build());
		Member member = getMember("test id", "test1@test.com");
		Category category = getCategory(categoryName1);
		Item item1 = itemRepository.save(
			getItem(category, member));

		Item item2 = itemRepository.save(
			getItem(category, member));

		item1.setImageItems(new ArrayList<>(List.of(
			getImageItem(url1, item1), getImageItem(url2, item1))));
		item1.setImageChecks(imageCheckRepository.saveAll(new ArrayList<>(List.of(
			getImageCheck(url1, item1), getImageCheck(url2, item1)))));
		item1.setStyleItems(styleItemRepository.saveAll(new ArrayList<>(List.of(
			getStyleItem(styleSaved1, item1), getStyleItem(styleSaved2, item1)))));

		item2.setImageItems(new ArrayList<>(List.of(
			getImageItem(url3, item2), getImageItem(url4, item2))));
		item2.setImageChecks(imageCheckRepository.saveAll(new ArrayList<>(List.of(
			getImageCheck(url3, item2), getImageCheck(url4, item2)))));
		item2.setStyleItems(styleItemRepository.saveAll(new ArrayList<>(List.of(
			getStyleItem(styleSaved1, item2)))));

		//when
		List<Item> items1 = itemRepository.searchAllMyItem(
			new ArrayList<>(List.of(State.TEMPORARY)), member);

		List<Item> items2 = itemRepository.searchAllMyItem(
			new ArrayList<>(List.of(State.TEMPORARY)), getMember("another", "test2@test.com"));
		List<Item> items3 = itemRepository.searchAllMyItem(
			new ArrayList<>(List.of(State.ASSESSING)), member);

		//then
		assertEquals(items1.size(), 2);
		assertEquals(items2.size(), 0);
		assertEquals(items3.size(), 0);

		assertEquals(items1.get(1).getImageItems().size(), 2);
		assertEquals(items1.get(1).getImageItems().get(0).getUrl(), url1);
		assertEquals(items1.get(1).getImageItems().get(1).getUrl(), url2);
		assertEquals(items1.get(1).getImageChecks().size(), 2);
		assertEquals(items1.get(1).getImageChecks().get(0).getUrl(), url1);
		assertEquals(items1.get(1).getImageChecks().get(1).getUrl(), url2);
		assertEquals(items1.get(1).getStyleItems().size(), 2);
		assertEquals(items1.get(1).getStyleItems().get(0).getName(), style1);
		assertEquals(items1.get(1).getStyleItems().get(1).getName(), style2);

		assertEquals(items1.get(0).getImageItems().size(), 2);
		assertEquals(items1.get(0).getImageItems().get(0).getUrl(), url3);
		assertEquals(items1.get(0).getImageItems().get(1).getUrl(), url4);
		assertEquals(items1.get(0).getImageChecks().size(), 2);
		assertEquals(items1.get(0).getImageChecks().get(0).getUrl(), url3);
		assertEquals(items1.get(0).getImageChecks().get(1).getUrl(), url4);
		assertEquals(items1.get(0).getStyleItems().size(), 1);
		assertEquals(items1.get(0).getStyleItems().get(0).getName(), style1);
	}

	@DisplayName("01_01. searchAllMyItem success size 0")
	@Test
	public void test_01_01() {
		//given
		Member member = getMember("test id", "test@test.com");

		//when
		List<Item> items = itemRepository.searchAllMyItem(
			new ArrayList<>(List.of(State.TEMPORARY)), member);

		//then
		assertEquals(items.size(), 0);
	}

	String url1 = "url1";
	String url2 = "url2";
	String url3 = "url3";
	String url4 = "url4";
	String style1 = "style1";
	String style2 = "style2";
	String categoryName1 = "test category1";

	private Member getMember(String userId, String email) {
		return memberRepository.save(Member.builder()
			.userId(userId)
			.email(email)
			.name("name")
			.providerType(ProviderType.KAKAO)
			.role(Role.ADMIN)
			.build());
	}

	private Category getCategory(String name) {
		return categoryRepository.save(Category.builder()
			.name(name)
			.children(new ArrayList<>())
			.build());
	}

	private ImageItem getImageItem(String url, Item item) {
		return imageItemRepository.save(ImageItem.builder()
			.url(url)
			.item(item)
			.build());
	}

	private ImageCheck getImageCheck(String url, Item item) {
		return imageCheckRepository.save(ImageCheck.builder()
			.url(url)
			.item(item)
			.build());
	}

	private Item getItem(Category category, Member member) {
		return Item.builder()
			.name("test name")
			.member(member)
			.category(category)
			.startPrice(100L)
			.width(100L)
			.depth(100L)
			.height(100L)
			.material("나무")
			.conditionGrade("test CG")
			.conditionDescription("test CD")
			.text("test text")
			.madeIn("test madeIn")
			.designer("test designer")
			.brand("test brand")
			.productionYear(2023)
			.state(State.TEMPORARY)
			.created(LocalDateTime.now())
			.updated(LocalDateTime.now())
			.imageItems(new ArrayList<>())
			.imageChecks(new ArrayList<>())
			.styleItems(new ArrayList<>())
			.build();
	}

	private StyleItem getStyleItem(Style style, Item item) {
		return styleItemRepository.save(StyleItem.builder()
			.style(style)
			.name(style.getName())
			.item(item)
			.build());
	}
}