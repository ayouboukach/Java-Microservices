package com.learning.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.learning.model.entity.Privilege;
import com.learning.model.entity.Role;

@Repository
public interface IPrivilegeRepository extends CrudRepository<Privilege, Long> {

	List<Privilege> findByRoles(Role role);
}
