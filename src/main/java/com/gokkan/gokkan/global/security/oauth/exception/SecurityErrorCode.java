package com.gokkan.gokkan.global.security.oauth.exception;

import com.gokkan.gokkan.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {
	/*
	* } catch (SecurityException e) {
			log.info("Invalid JWT signature.");
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
		}
	* */
	INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "Invalid JWT signature."),
	INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid JWT token."),
	EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Expired JWT token."),
	UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Unsupported JWT token."),
	INVALID_JWT_TOKEN_COMPACT(HttpStatus.UNAUTHORIZED, "JWT token compact of handler are invalid.");

	private final HttpStatus httpStatus;
	private final String message;
}
