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

		String exception = (String) request.getAttribute("exception");
		if (exception.equals(SecurityErrorCode.EXPIRED_JWT_TOKEN.getMessage())) {
			setResponse(response, SecurityErrorCode.EXPIRED_JWT_TOKEN);
		}
		if (exception.equals(SecurityErrorCode.INVALID_JWT_SIGNATURE.getMessage())) {
			setResponse(response, SecurityErrorCode.INVALID_JWT_SIGNATURE);
		}
		if (exception.equals(SecurityErrorCode.INVALID_JWT_TOKEN.getMessage())) {
			setResponse(response, SecurityErrorCode.INVALID_JWT_TOKEN);
		}
		if (exception.equals(SecurityErrorCode.UNSUPPORTED_JWT_TOKEN.getMessage())) {
			setResponse(response, SecurityErrorCode.UNSUPPORTED_JWT_TOKEN);
		}
		if (exception.equals(SecurityErrorCode.ILLEGAL_ARGUMENT_JWT_TOKEN.getMessage())) {
			setResponse(response, SecurityErrorCode.ILLEGAL_ARGUMENT_JWT_TOKEN);
		}

//		authException.printStackTrace();
//		log.info("Responding with unauthorized error. Message := {}",
//			authException.getMessage());
//		response.sendError(
//			HttpServletResponse.SC_UNAUTHORIZED,
//			authException.getLocalizedMessage()
//		);

	}

	private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
			+ "\", \"code\" : \"" + errorCode.name()
			+ "\", \"status\" : " + errorCode.getHttpStatus()
			+ ", \"errors\" : [ ] }");
	}


}

