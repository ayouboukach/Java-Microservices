package com.learning.service;

import java.util.List;

import com.learning.model.entity.Privilege;

public interface IPrivilegeService {

	Privilege addNewPrivilege(Privilege privilege);

	List<Privilege> addList(List<Privilege> privileges);

	List<Privilege> getAllPrivileges();
}
