package com.gokkan.gokkan.global.security.oauth.filter;

import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.member.repository.MemberRepository;
import com.gokkan.gokkan.global.security.oauth.exception.SecurityErrorCode;
import com.gokkan.gokkan.global.security.oauth.token.AuthToken;
import com.gokkan.gokkan.global.security.oauth.token.AuthTokenProvider;
import com.gokkan.gokkan.infra.utils.HeaderUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final AuthTokenProvider tokenProvider;
	private final MemberRepository memberRepository;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String tokenStr = HeaderUtil.getAccessToken(request);
		AuthToken token = tokenProvider.convertAuthToken(tokenStr);

		if (token.validate()) {
			log.info("토큰 유효 멤버 조회");
			String userId = "";
			try {
				userId = Jwts.parserBuilder()
					.setSigningKey(tokenProvider.getKey())
					.build()
					.parseClaimsJws(token.getToken())
					.getBody().getSubject();
			} catch (SecurityException e) {
				log.error("Invalid JWT signature.");
				request.setAttribute("exception",
					SecurityErrorCode.INVALID_JWT_SIGNATURE.getHttpStatus());
			} catch (MalformedJwtException e) {
				log.error("Invalid JWT token.");
				request.setAttribute("exception",
					SecurityErrorCode.INVALID_JWT_TOKEN.getHttpStatus());
			} catch (ExpiredJwtException e) {
				log.error("Expired JWT token.");
				request.setAttribute("exception",
					SecurityErrorCode.EXPIRED_JWT_TOKEN.getHttpStatus());
			} catch (UnsupportedJwtException e) {
				log.error("Unsupported JWT token.");
				request.setAttribute("exception",
					SecurityErrorCode.UNSUPPORTED_JWT_TOKEN.getHttpStatus());
			} catch (IllegalArgumentException e) {
				log.error("JWT token compact of handler are invalid.");
				request.setAttribute("exception",
					SecurityErrorCode.INVALID_JWT_TOKEN_COMPACT.getHttpStatus());
			}
			if (Objects.equals(userId, "")) {
				request.setAttribute("exception", MemberErrorCode.MEMBER_NOT_FOUND.getHttpStatus());
			}
			Member member = memberRepository.findByUserId(userId).orElseThrow();
			Authentication authentication = tokenProvider.getAuthentication(token, member);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}

