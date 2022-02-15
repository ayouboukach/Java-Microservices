package com.learning.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learning.model.entity.Role;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "api-role")
public interface IRoleServiceClient {
	
	@GetMapping("/role/{id}")
	@Retry(name="api-role")
	@CircuitBreaker(name="api-role", fallbackMethod="getRoleFallback")
	public Role getRole(@PathVariable String id);
	
	default Role getRoleFallback(String id, Throwable exception){
		System.out.println("Param = " + id);
		System.out.println("Exception class=" + exception.getClass().getName());
		System.out.println("Exception took place: " + exception.getMessage());
		return new Role();
	}
} 