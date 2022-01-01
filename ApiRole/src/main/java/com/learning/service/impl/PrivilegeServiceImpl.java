package com.learning.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.model.entity.Privilege;
import com.learning.repository.IPrivilegeRepository;
import com.learning.service.IPrivilegeService;

@Service
@Transactional
@Qualifier
public class PrivilegeServiceImpl implements IPrivilegeService {

	private IPrivilegeRepository privilegeRepository;

	@Autowired
	public PrivilegeServiceImpl(IPrivilegeRepository privilegeRepository) {
		this.privilegeRepository = privilegeRepository;
	}

	@Override
	public Privilege addNewPrivilege(Privilege privilege) {
		return privilegeRepository.save(privilege);
	}

//	private String generateRoleId() {
//		return RandomStringUtils.randomNumeric(10);
//	}

	@Override
	public List<Privilege> getAllPrivileges() {
		return (List<Privilege>) privilegeRepository.findAll();
	}

	@Override
	public List<Privilege> addList(List<Privilege> privileges) {
		return (List<Privilege>) privilegeRepository.saveAll(privileges);
	}

}
