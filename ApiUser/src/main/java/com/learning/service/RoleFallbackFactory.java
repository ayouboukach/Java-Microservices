package com.learning.service;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RoleFallbackFactory implements FallbackFactory<IRoleServiceClient>{

	@Override
	public IRoleServiceClient create(Throwable cause) {
		// TODO Auto-generated method stub
		return new RoleFallback(cause);
	}

}
