package com.gokkan.gokkan.domain.style.service;

import com.gokkan.gokkan.domain.style.repository.ExpertStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpertStyleService {

	private final ExpertStyleRepository expertStyleRepository;
}
