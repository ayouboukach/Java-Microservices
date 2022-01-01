package com.learning.service;

import org.springframework.stereotype.Component;

import com.learning.model.entity.Role;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoleFallback implements IRoleServiceClient {

	private final Throwable cause;

	@Override
	public Role getRole(String id) {

		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			log.error("404 error took place when getRole was called with roleId: " + id + ". Error message: "
					+ cause.getLocalizedMessage());
		} else {
			log.error("Other error took place: " + cause.getLocalizedMessage());
		}
		return new Role();
	}

}
