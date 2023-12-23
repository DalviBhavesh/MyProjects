package com.springboot.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.dao.ContactRepository;
import com.springboot.dao.UserRepository;
import com.springboot.entities.Contact;
import com.springboot.entities.User;

@RestController
public class SearchRestController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/search/{keyword}")
	public ResponseEntity<?> Search(@PathVariable("keyword")String keyword, Principal principal){
		
		System.out.println(keyword);
		User user = this.userRepository.getUserByUserName(principal.getName());
		
		List<Contact> contact= this.contactRepository.findContactsByKeywordsAndUser(keyword, user.getId());
		
		System.out.println(contact);
		
		return ResponseEntity.ok(contact);
		
	}
	
}
