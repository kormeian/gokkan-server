package com.gokkan.gokkan.domain.member.controller;

import com.gokkan.gokkan.domain.member.domain.MemberRefreshToken;
import com.gokkan.gokkan.domain.member.exception.AuthErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRefreshTokenRepository;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.security.config.properties.AppProperties;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import com.gokkan.gokkan.global.security.oauth.token.AuthToken;
import com.gokkan.gokkan.global.security.oauth.token.AuthTokenProvider;
import com.gokkan.gokkan.infra.utils.CookieUtil;
import com.gokkan.gokkan.infra.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private static final long THREE_DAYS_MSEC = 259200000;
	private static final String REFRESH_TOKEN = "refresh_token";
	private final AppProperties appProperties;
	private final AuthTokenProvider tokenProvider;
	private final MemberRefreshTokenRepository memberRefreshTokenRepository;

	@GetMapping("/refresh")
	public ResponseEntity<String> refreshToken(HttpServletRequest request,
		HttpServletResponse response) {
		// access token 확인
		String accessToken = HeaderUtil.getAccessToken(request);
		AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
		if (!authToken.validate()) {
			throw new RestApiException(AuthErrorCode.ACCESS_TOKEN_INVALID);
		}

		// expired access token 인지 확인
		Claims claims = authToken.getExpiredTokenClaims();

		String userId = claims.getSubject();
		Role roleType = Role.of(claims.get("role", String.class));

		// userId refresh token 으로 DB 확인
		MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByUserId(userId);
		if (memberRefreshToken == null) {
			throw new RestApiException(AuthErrorCode.REFRESH_TOKEN_INVALID);
		}
		AuthToken authRefreshToken = tokenProvider.convertAuthToken(
			memberRefreshToken.getRefreshToken());

		Date now = new Date();
		AuthToken newAccessToken = tokenProvider.createAuthToken(
			userId,
			roleType.getCode(),
			new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
		);

		long validTime =
			authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

		// refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
		if (validTime <= THREE_DAYS_MSEC) {
			// refresh 토큰 설정
			long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

			authRefreshToken = tokenProvider.createAuthToken(
				appProperties.getAuth().getTokenSecret(),
				new Date(now.getTime() + refreshTokenExpiry)
			);

			// DB에 refresh 토큰 업데이트
			memberRefreshToken.setRefreshToken(authRefreshToken.getToken());

			int cookieMaxAge = (int) refreshTokenExpiry / 60;
			CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
			CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(),
				cookieMaxAge);
		}

		return ResponseEntity.ok(newAccessToken.getToken());
	}
}

