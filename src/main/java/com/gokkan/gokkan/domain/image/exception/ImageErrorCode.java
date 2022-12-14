package com.gokkan.gokkan.domain.image.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageErrorCode {

	EMPTY_FILE("저장할 이미지가 없습니다."),
	EMPTY_URL("저장할 이미지 url이 없습니다."),
	INTERNAL_SERVER_ERROR("이미지 업로드에 실패하였습니다."),
	MISMATCH_FILE_TYPE("잘못된 형식의 파일입니다."),
	NOT_DELETED_IMAGE("오류가 발생하여 이미지가 삭제되지 않았습니다."),
	INVALID_FORMAT_FILE("png, jpg, jpeg 형식의 파일이 아닙니다."),
	INVALID_FORMAT_URL("잘못된 이미지 url입니다."),
	NOT_FOUND_IMAGE_ITEM("해당 아이디의 상품 이미지를 찾을 수 없습니다."),
	NOT_FOUND_IMAGE_CHECK("해당 아이디의 검수 이미지를 찾을 수 없습니다.");


	private final String description;
}
