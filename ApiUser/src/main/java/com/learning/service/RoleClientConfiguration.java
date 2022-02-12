package com.learning.service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleClientConfiguration {
	
	private final CircuitBreakerRegistry registry;
    private final RoleFallback fallbackRole;
//    
//    @Bean
//    @Scope("prototype")
//    public Feign.Builder feignBuilder() {
//        CircuitBreaker circuitBreaker = registry.circuitBreaker("api-role");
//        FeignDecorators decorators = FeignDecorators.builder()
//                .withCircuitBreaker(circuitBreaker)
//                .withFallback(fallbackRole)
//                .build();
//        return Resilience4jFeign.builder(decorators);
//    }
}
