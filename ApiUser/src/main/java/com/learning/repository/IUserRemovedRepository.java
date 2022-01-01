package com.learning.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.learning.model.entity.UserRemoved;

@Repository
public interface IUserRemovedRepository extends CrudRepository<UserRemoved, Long> {

	UserRemoved findByUsername(String username);

	UserRemoved findByEmail(String email);
}
