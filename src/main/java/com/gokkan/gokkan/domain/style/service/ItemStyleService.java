package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.style.repository.ItemStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemStyleService {

	private final ItemStyleRepository itemStyleRepository;
}
