package com.learning.security.jwt.filter;

import static com.learning.constant.SecurityConstant.OPTIONS_HTTP_METHOD;
import static com.learning.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.learning.security.jwt.JwtTokenProvider;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private JwtTokenProvider jwtTokenProvider;

	public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
			response.setStatus(OK.value());
		} else {
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}
			String token = authorizationHeader.substring(TOKEN_PREFIX.length());
			String username = jwtTokenProvider.getSubject(token);
			if (jwtTokenProvider.isTokenValid(username, token)
					&& SecurityContextHolder.getContext().getAuthentication() == null) {
				Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
						new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.clearContext();
			}
		}
		filterChain.doFilter(request, response);
	}

}