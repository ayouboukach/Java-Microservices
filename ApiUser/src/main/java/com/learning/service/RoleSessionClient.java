package com.learning.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learning.model.entity.Role;

@FeignClient(name = "api-role",fallbackFactory = RoleSessionClientFallbackFactory.class)
public interface RoleSessionClient {

	@GetMapping("/role/{id}")
	public Role getRole(@PathVariable String id);
}
