package com.learning.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.model.entity.Role;
import com.learning.repository.IRoleRepository;
import com.learning.service.IRoleService;

@Service
@Transactional
@Qualifier
public class RoleServiceImpl implements IRoleService{

	private IRoleRepository roleRepository;
	
	@Autowired
	public RoleServiceImpl(IRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role addNewRole(Role role) {
//		role.setRoleid(generateRoleId());
		return roleRepository.save(role);
	}
	
//	private String generateRoleId() {
//		return RandomStringUtils.randomNumeric(10);
//	}

	@Override
	public List<Role> getAllRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	
	@Override
	public Role getRole(String id) {
		return roleRepository.findRoleById(id);
	}

	@Override
	public Role UpdateRole(Role role) {
		return roleRepository.save(role);
	}
	
	
	@Override
	public List<Role> addNewRoles(List<Role> roles) {
//		role.setRoleid(generateRoleId());
		return (List<Role>) roleRepository.saveAll(roles);
	}
}
