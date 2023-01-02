package com.gokkan.gokkan.domain.item.repository;

import com.gokkan.gokkan.domain.item.dto.ItemDto.ListResponse;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {

	List<ListResponse> searchAllItemForExport(Member member);

	List<ListResponse> searchAllMyItem(List<State> state, Member member);
}
