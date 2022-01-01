package com.learning.service;

import java.util.List;

import com.learning.model.entity.Role;

public interface IRoleService {

	Role addNewRole(Role role);
	
	Role UpdateRole(Role role);

	List<Role> getAllRoles();

	List<Role> addNewRoles(List<Role> roles);

	Role getRole(String id);

//	public Role getRole(Long id);
}
