package com.learning.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.learning.model.entity.Role;

@Repository
public interface IRoleRepository extends CrudRepository<Role, Long> {

//	@Modifying
//	@Query(value = "insert into roles_privileges (roleid,privilegeid) VALUES (:idrole,:idprivilege)", nativeQuery = true)
//	void SetPrivilegeOnRole(@Param("idrole") String idrole, @Param("idprivilege") String idprivilege);
//    User findByUsername(String username);
//
    Role findRoleById(String id);
}
