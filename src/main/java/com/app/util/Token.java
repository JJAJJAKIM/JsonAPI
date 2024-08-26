package com.app.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@Component
public class Token {

	private final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

	@Value("${access.auth.jwt}")
	private String jwtSecretKey;

	@Value("${access.auth.interval}")
	private int interval;
	
	public String setToken(Authentication auth) {
		
		JwtBuilder token = Jwts.builder()
				.header().add(getHeader()).and()
				.claims(setClaims(auth))
				.expiration(getDate())
				.issuedAt(Calendar.getInstance().getTime())
			.signWith(getKey(), ALGORITHM);
		
		return token.compact();
	}
	
	private Map<String, String> getHeader() {
		Map<String, String> header = new HashMap<>();
		header.put("alg", "HS256");
		header.put("typ", "JWT");
		return header;
	}
	
	private SecretKey getKey() {
//		return Jwts.SIG.HS256.key().build();
	    return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecretKey));
//		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
	}
	
	/********************************************************************
	 * iss : 토큰 발급자	(issuer)
	 * sub : 토큰 제목		(subject)
	 * aud : 토큰 대상자	(audience)
	 * exp : 토큰 만료 시간	(expiration)
	 * iat : 토큰 발급 시간	(issuedAt)
	 * nbf : 토큰 활성 날짜	(not before)
	 ********************************************************************/
	private Map<String, ?> setClaims(Authentication auth) {
		Map<String, Object> claims = new HashMap<>();

		Map<String, Object> user = new HashMap<>();
		user.put("userNm", "TEST");
//		user.put("userNm", auth.getName());
//		user.put("roles", auth.getAuthorities());
		claims.put("issuer", "JsonAPI");
		claims.put("subject", "User");
		claims.put("audience", user);
//		claims.put("expiration", getDate());
//		claims.put("issuedAt", Calendar.getInstance().getTime());
				
		return claims;
	}
	
	private Date getDate() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MINUTE, interval);
		return date.getTime();
	}
	
	public Claims getToken(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		log.info("Claims : {}", claims.get("audience"));
		return claims;
	}

	public boolean isValidToken(String token) {
		try {
			if(token != null || !token.isEmpty()) {
				Claims claims = getToken(token);
				log.info("============================================");
				log.info("|ExpireTime\t: {}|", claims.getExpiration());
				log.info("|IssuedTime: {}|", claims.getIssuedAt());
				log.info("|RealTime\t: {}|", Calendar.getInstance().getTime());
				log.info("============================================");
				return true;
			}
		} catch (ExpiredJwtException exception) {
			log.info("==============");
			log.error("Token Expired");
		} catch (JwtException exception) {
			log.info("==============");
			log.error("Token Tampered");
		} catch (NullPointerException exception) {
			log.info("==============");
			log.error("Token is null");
		}
		log.info("==============");
		return false;
	}
}
