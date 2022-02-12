package com.learning.service;

import org.springframework.stereotype.Component;

import com.learning.model.entity.Role;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RoleSessionClientFallback implements RoleSessionClient{
	
	
	@Override
	public Role getRole(String id) {
        log.info("[Fallback] validateSession");
		return new Role();
	}

}
