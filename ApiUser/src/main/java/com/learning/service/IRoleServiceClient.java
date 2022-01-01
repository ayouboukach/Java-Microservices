package com.learning.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learning.model.entity.Role;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(name = "api-role", configuration = RoleClientConfiguration.class)
public interface IRoleServiceClient {

	@CircuitBreaker(name = "api-role")
	@GetMapping("/role/{id}")
	public Role getRole(@PathVariable String id);
}