package com.gokkan.gokkan.domain.item.repository;

import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {

	List<Item> searchAllItemForExport(Member member);

	List<Item> searchAllMyItem(List<State> state, Member member);
}
