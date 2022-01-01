package com.learning.security.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.learning.constant.SecurityConstant.BOOK_LIBRARY_LLC;
import static com.learning.constant.SecurityConstant.TOKEN_CANNOT_BE_VERIFIED;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtTokenProvider {

	private Environment env;

	@Autowired
	public JwtTokenProvider(Environment env) {
		this.env = env;
	}

	public boolean isTokenValid(String username, String token) {
		JWTVerifier verifier = getJWTVerifier();
		return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
	}

	public String getSubject(String token) {
		JWTVerifier verifier = getJWTVerifier();
		return verifier.verify(token).getSubject();
	}

	private boolean isTokenExpired(JWTVerifier verifier, String token) {
		Date expiration = verifier.verify(token).getExpiresAt();
		return expiration.before(new Date());
	}

	private JWTVerifier getJWTVerifier() {
		JWTVerifier verifier;
		try {
			Algorithm algorithm = HMAC512(env.getProperty("jwt.jwtkeysecret"));
			verifier = JWT.require(algorithm).withIssuer(BOOK_LIBRARY_LLC).build();
		} catch (JWTVerificationException exception) {
			throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
		}
		return verifier;
	}
}
