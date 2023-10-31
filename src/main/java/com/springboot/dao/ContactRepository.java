package com.springboot.dao;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.entities.Contact;



public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Query("select c from Contact c where c.user.id =:userId")
	public List<Contact> findContactsById(@Param("userId")int userId);
	
	
	//implementing pagination on above method and using in view all contacts controller
	
	@Query("select c from Contact c where c.user.id =:userId")
	//current page - page
	//contact per page - 5
	public Page<Contact> findPageableContactsById(@Param("userId")int userId,Pageable perPageable);

	
	@Modifying
	@Transactional
	@Query("delete from Contact c where c.cId =:id")
	public void deleteContactById(@Param("id")int id);
	
	
}
