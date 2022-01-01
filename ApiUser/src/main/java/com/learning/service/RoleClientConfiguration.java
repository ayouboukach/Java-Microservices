package com.learning.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import feign.Feign;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleClientConfiguration {
	
	private final CircuitBreakerRegistry registry;
    private final RoleFallback fallbackRole;
    
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        CircuitBreaker circuitBreaker = registry.circuitBreaker("api-role");
        FeignDecorators decorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreaker)
                .withFallback(fallbackRole)
                .build();
        return Resilience4jFeign.builder(decorators);
    }
}
