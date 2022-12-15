package com.gokkan.gokkan.domain.heart.service;

import com.gokkan.gokkan.domain.heart.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

	private final HeartRepository heartRepository;

}
