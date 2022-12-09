package com.gokkan.gokkan.domain.style.service;

import static com.gokkan.gokkan.domain.style.dto.StyleDto.Response;

import com.gokkan.gokkan.domain.style.domain.Style;
import com.gokkan.gokkan.domain.style.dto.StyleDto.CreateRequest;
import com.gokkan.gokkan.domain.style.dto.StyleDto.UpdateRequest;
import com.gokkan.gokkan.domain.style.exception.StyleErrorCode;
import com.gokkan.gokkan.domain.style.exception.StyleException;
import com.gokkan.gokkan.domain.style.repository.StyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StyleService {

	private final StyleRepository styleRepository;

	public Response create(CreateRequest request) {
		return Response.toResponse(
			styleRepository.save(
				Style.builder()
					.name(request.getName())
					.build()));
	}

	@Transactional(readOnly = true)
	public Response read(String name) {
		return Response.toResponse(
			getStyleByName(name));
	}

	public boolean delete(String name) {
		styleRepository.delete(getStyleByName(name));
		return true;
	}

	public Response update(UpdateRequest request) {
		Style style = getStyleById(request);
		style.setName(request.getName());
		return Response.toResponse(styleRepository.save(style));
	}

	private Style getStyleByName(String name) {
		return styleRepository.findByName(name).orElseThrow(() -> new StyleException(
			StyleErrorCode.NOT_FOUND_STYLE));
	}

	private Style getStyleById(UpdateRequest request) {
		return styleRepository.findById(request.getId()).orElseThrow(() -> new StyleException(
			StyleErrorCode.NOT_FOUND_STYLE));
	}
}
