package com.learning.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.learning.model.entity.User;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);

//	User findById(long id);

	User findByUserId(String userId);

	void deleteByUserId(String userId);
	
	void deleteByUsername(String username);
}
