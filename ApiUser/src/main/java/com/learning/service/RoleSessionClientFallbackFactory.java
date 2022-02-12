package com.learning.service;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.learning.model.entity.Role;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RoleSessionClientFallbackFactory implements FallbackFactory<RoleSessionClient> {

	@Override
	public RoleSessionClient create(Throwable cause) {
		log.error("An exception occurred when calling the UserSessionClient"+ cause.getLocalizedMessage());
		return new RoleSessionClient() {
			@Override
			public Role getRole(String id) {
				log.info("[Fallback] validateSession");
				return new Role();
			}
		};
	}

}
