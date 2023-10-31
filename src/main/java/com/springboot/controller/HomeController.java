package com.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.dao.UserRepository;
import com.springboot.entities.Contact;
import com.springboot.entities.User;
import com.springboot.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller

public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	//test controller for persistence logic testing
	@GetMapping("/test")
	//@ResponseBody
	public String test() {
		
		User user1 = new User();
		user1.setName("Bhavesh");
		user1.setEmail("dalvibhavesh007@gmail.com");
		
		Contact c1 = new Contact();
		c1.setName("kamal");
		c1.setEmail("kamal@gmail.com");
		 
		
		Contact c2 = new Contact();
		c2.setName("prisha");
		c2.setEmail("prisha@gmail.com");
		
		
		
		List<Contact> list = new ArrayList<>();
		list.add(c1);
		list.add(c2);
		
		user1.setContacts(list);
		
		this.userRepository.save(user1);
			
		return "persistence logic working";
	}
	
	
	//home page controller
	@GetMapping("/")
	public String home() {
		
		return "index";
	}
	
	//about page controller
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	//signUp controller
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user",new User());
		
		return "signup";
	}
	
	@PostMapping("/doSignup")
	public String doSignup(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(value="agreement" ,defaultValue="false")boolean agreement, Model model, HttpSession session) 
	{
		try {
			if(!agreement) {
				System.out.println("You have not Agreed the terms and conditions...");
				session.setAttribute("message",new Message("Agree the terms and conditions...","alert alert-danger"));
				//throw new Exception("You have not Agreed the terms and conditions...");
				return "signup";
			}
			
			if(result.hasErrors()) {
				System.out.print("Error: "+result.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setImageUrl("profile.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement: "+agreement);
			System.out.println("user: "+user);
			
			this.userRepository.save(user);
			
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("SignUp Sucessfull !!","alert alert-success"));
			return "redirect:/login";
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message",new Message("User already present! Use different email Id & Try again...","alert alert-danger"));
		}
		
		return "signup";
	}
	
}
