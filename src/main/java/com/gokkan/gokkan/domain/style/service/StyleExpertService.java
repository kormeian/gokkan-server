package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.style.repository.StyleExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StyleExpertService {

	private final StyleExpertRepository styleExpertRepository;
}
