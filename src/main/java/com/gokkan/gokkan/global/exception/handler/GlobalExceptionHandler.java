package com.gokkan.gokkan.global.exception.handler;

import com.gokkan.gokkan.global.exception.errorcode.CommonErrorCode;
import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.exception.response.ErrorResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RestApiException.class)
	public ResponseEntity<Object> handleQuizException(final RestApiException e) {
		final ErrorCode errorCode = e.getErrorCode();
		log.warn("RestApiException : " + errorCode.getHttpStatus().value() + " ("
			+ errorCode.getMessage() + ")");
		return handleExceptionInternal(errorCode);
	}

	@MessageExceptionHandler
	@SendTo("/queue/error")
	public String handleMessageException(final RestApiException e) throws JSONException {
		final ErrorCode errorCode = e.getErrorCode();
		log.warn("RestApiException : " + errorCode.getHttpStatus().value() + " ("
			+ errorCode.getMessage() + ")");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", errorCode.getHttpStatus().value());
		jsonObject.put("code", errorCode.name());
		jsonObject.put("message", errorCode.getMessage());
		return jsonObject.toString();

	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e) {
		log.warn("handleIllegalArgument", e);
		final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(errorCode, e.getMessage());
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
		final MethodArgumentNotValidException e,
		final HttpHeaders headers,
		final HttpStatus status,
		final WebRequest request) {
		log.warn("handleIllegalArgument", e);
		final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(e, errorCode);
	}

//    @ExceptionHandler({ConstraintViolationException.class})
//    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException e) {
//        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
//        return handleExceptionInternal(e, errorCode);
//    }

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAllException(final Exception ex) {
		log.warn("handleAllException", ex);
		final ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(errorCode);
	}

	private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode));
	}

	private ErrorResponse makeErrorResponse(final ErrorCode errorCode) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.build();
	}

	private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode,
		final String message) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode, message));
	}

	private ErrorResponse makeErrorResponse(final ErrorCode errorCode, final String message) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(message)
			.build();
	}

	private ResponseEntity<Object> handleExceptionInternal(final BindException e,
		final ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(e, errorCode));
	}

	private ErrorResponse makeErrorResponse(final BindException e, final ErrorCode errorCode) {
		final List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(ErrorResponse.ValidationError::of)
			.collect(Collectors.toList());

		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.errors(validationErrorList)
			.build();
	}

//    private ResponseEntity<Object> handleExceptionInternal(final ConstraintViolationException e, final ErrorCode errorCode) {
//        return ResponseEntity.status(errorCode.getHttpStatus())
//                .body(makeErrorResponse(e, errorCode));
//    }

//    private ErrorResponse makeErrorResponse(final ConstraintViolationException e, final ErrorCode errorCode) {
//        final List<ErrorResponse.ValidationError> validationErrorList = e.getConstraintViolations()
//                .stream()
//                .map(ErrorResponse.ValidationError::of)
//                .collect(Collectors.toList());
//
//        return ErrorResponse.builder()
//                .code(errorCode.name())
//                .message(errorCode.getMessage())
//                .errors(validationErrorList)
//                .build();
//    }

}
