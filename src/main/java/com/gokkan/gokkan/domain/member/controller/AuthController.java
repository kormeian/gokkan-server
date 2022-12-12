package com.gokkan.gokkan.domain.member.controller;

import com.gokkan.gokkan.domain.member.domain.AuthReqModel;
import com.gokkan.gokkan.domain.member.domain.MemberRefreshToken;
import com.gokkan.gokkan.domain.member.repository.MemberRefreshTokenRepository;
import com.gokkan.gokkan.global.security.common.ApiResponse;
import com.gokkan.gokkan.global.security.config.properties.AppProperties;
import com.gokkan.gokkan.global.security.oauth.entity.RoleType;
import com.gokkan.gokkan.global.security.oauth.entity.UserPrincipal;
import com.gokkan.gokkan.global.security.oauth.token.AuthToken;
import com.gokkan.gokkan.global.security.oauth.token.AuthTokenProvider;
import com.gokkan.gokkan.infra.utils.CookieUtil;
import com.gokkan.gokkan.infra.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final static long THREE_DAYS_MSEC = 259200000;
	private final static String REFRESH_TOKEN = "refresh_token";
	private final AppProperties appProperties;
	private final AuthTokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;
	private final MemberRefreshTokenRepository memberRefreshTokenRepository;

	@PostMapping("/login")
	public ApiResponse login(
		HttpServletRequest request,
		HttpServletResponse response,
		@RequestBody AuthReqModel authReqModel
	) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				authReqModel.getId(),
				authReqModel.getPassword()
			)
		);

		String userId = authReqModel.getId();
		SecurityContextHolder.getContext().setAuthentication(authentication);

		Date now = new Date();
		AuthToken accessToken = tokenProvider.createAuthToken(
			userId,
			((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(),
			new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
		);

		long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
		AuthToken refreshToken = tokenProvider.createAuthToken(
			appProperties.getAuth().getTokenSecret(),
			new Date(now.getTime() + refreshTokenExpiry)
		);

		// userId refresh token 으로 DB 확인
		MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByUserId(userId);
		if (memberRefreshToken == null) {
			// 없는 경우 새로 등록
			memberRefreshToken = new MemberRefreshToken(userId, refreshToken.getToken());
			memberRefreshTokenRepository.saveAndFlush(memberRefreshToken);
		} else {
			// DB에 refresh 토큰 업데이트
			memberRefreshToken.setRefreshToken(refreshToken.getToken());
		}

		int cookieMaxAge = (int) refreshTokenExpiry / 60;
		CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
		CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

		return ApiResponse.success("token", accessToken.getToken());
	}

	@GetMapping("/refresh")
	public ApiResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
		// access token 확인
		String accessToken = HeaderUtil.getAccessToken(request);
		AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
		if (!authToken.validate()) {
			return ApiResponse.invalidAccessToken();
		}

		// expired access token 인지 확인
		Claims claims = authToken.getExpiredTokenClaims();
		if (claims == null) {
			return ApiResponse.notExpiredTokenYet();
		}

		String userId = claims.getSubject();
		RoleType roleType = RoleType.of(claims.get("role", String.class));

		// refresh token
		String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
			.map(Cookie::getValue)
			.orElse((null));
		AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

		if (authRefreshToken.validate()) {
			return ApiResponse.invalidRefreshToken();
		}

		// userId refresh token 으로 DB 확인
		MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByUserIdAndRefreshToken(
			userId, refreshToken);
		if (memberRefreshToken == null) {
			return ApiResponse.invalidRefreshToken();
		}

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

		return ApiResponse.success("token", newAccessToken.getToken());
	}
}

