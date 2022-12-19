package com.gokkan.gokkan.global.security.oauth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

	private static final String AUTHORITIES_KEY = "role";
	@Getter
	private final String token;
	private final Key key;

	AuthToken(String id, Date expiry, Key key) {
		this.key = key;
		this.token = createAuthToken(id, expiry);
	}

	AuthToken(String id, String role, Date expiry, Key key) {
		this.key = key;
		this.token = createAuthToken(id, role, expiry);
	}

	private String createAuthToken(String id, Date expiry) {
		return Jwts.builder()
			.setSubject(id)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	private String createAuthToken(String id, String role, Date expiry) {
		return Jwts.builder()
			.setSubject(id)
			.claim(AUTHORITIES_KEY, role)
			.signWith(key, SignatureAlgorithm.HS256)
			.setExpiration(expiry)
			.compact();
	}

	public boolean validate() {
		return this.getTokenClaims() != null;
	}

	public Claims getTokenClaims() {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public Claims getExpiredTokenClaims() {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			return e.getClaims();
		}
		return null;
	}
}
