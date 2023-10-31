package com.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("select u from User u where u.email =:email")//**Don't use space between =:email
	public User getUserByUserName(@Param("email") String email);
	
}
