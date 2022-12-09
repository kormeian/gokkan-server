package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StyleService {

	private final StyleRepository styleRepository;
}
