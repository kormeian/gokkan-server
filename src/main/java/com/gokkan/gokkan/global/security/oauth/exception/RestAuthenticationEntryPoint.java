package com.gokkan.gokkan.global.security.oauth.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException) throws IOException {

		Object exception = request.getAttribute("exception");
		findErrorCode(response, exception);
		log.info("exception : {}", exception);

		authException.printStackTrace();
		log.info("Responding with unauthorized error. Message := {}", authException.getMessage());
		response.sendError(
			HttpServletResponse.SC_UNAUTHORIZED,
			authException.getLocalizedMessage()
		);
	}

	private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
			+ "\", \"code\" : \"" + errorCode.getHttpStatus()
			+ "\", \"status\" : " + errorCode.getMessage()
			+ ", \"errors\" : [ ] }");
	}

	private void findErrorCode(HttpServletResponse response, Object exception) throws IOException {
		ErrorCode errorCode;
		if (exception.equals(SecurityErrorCode.EXPIRED_JWT_TOKEN.getHttpStatus())) {
			errorCode = SecurityErrorCode.EXPIRED_JWT_TOKEN;
			setResponse(response, errorCode);
		}
		if (exception.equals(SecurityErrorCode.INVALID_JWT_SIGNATURE.getHttpStatus())) {
			errorCode = SecurityErrorCode.INVALID_JWT_SIGNATURE;
			setResponse(response, errorCode);
		}
		if (exception.equals(SecurityErrorCode.INVALID_JWT_TOKEN.getHttpStatus())) {
			errorCode = SecurityErrorCode.INVALID_JWT_TOKEN;
			setResponse(response, errorCode);
		}
		if (exception.equals(SecurityErrorCode.UNSUPPORTED_JWT_TOKEN.getHttpStatus())) {
			errorCode = SecurityErrorCode.UNSUPPORTED_JWT_TOKEN;
			setResponse(response, errorCode);
		}
		if (exception.equals(SecurityErrorCode.INVALID_JWT_TOKEN_COMPACT.getHttpStatus())) {
			errorCode = SecurityErrorCode.INVALID_JWT_TOKEN_COMPACT;
			setResponse(response, errorCode);
		}
	}
}

