package com.springboot.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.dao.ContactRepository;
import com.springboot.dao.UserRepository;
import com.springboot.entities.Contact;
import com.springboot.entities.User;
import com.springboot.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	 private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void commonData(Model model,Principal principal) {
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		model.addAttribute("user", user);
	}
	
	@GetMapping("/home")
	public String home() {
		return "normal/home";
	}
	
	
	
	//show contactCards handler
	@RequestMapping("/showContactCards")
	public String viewContact(Model model,Principal principal) {
		
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		int uid = user.getId();
		
		List<Contact> list = contactRepository.findContactsById(uid);
		System.out.print(list);
		
		model.addAttribute("contactList", list);
		 
		return "normal/showContactCards";
	}
	
	
	//Individual card controller
	
	@GetMapping("/card/{id}")
	public String card(@PathVariable("id")Integer id,Model model,Principal principal) {
		
		
		Optional<Contact> contactOptional = contactRepository.findById(id);
		Contact contact = contactOptional.get();
		
		//fixing bug of resource leak
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		if(user.getId() == contact.getUser().getId()){
		model.addAttribute("contact", contact);
		}
		
		System.out.println(id);
		
		return "normal/card";
	}
	
	
	
	//add contact handler
	
	@GetMapping("/addContact")
	public String addContact(Model model) {
		
		model.addAttribute("contact",new Contact());
		
		return "normal/addContact";
	}
	
	@PostMapping("/processContact")
	public String addContact(@Valid @ModelAttribute Contact contact,BindingResult result,
							@RequestParam("profileImage") MultipartFile file ,
							Model model, Principal principal,HttpSession session) throws IOException {
		
		
		try {
			
					String username = principal.getName();
					User user = userRepository.getUserByUserName(username);
					
					contact.setUser(user);
					user.getContacts().add(contact);
					
					if(result.hasErrors()) {
						
						System.out.println("Errors: "+result.toString());
						model.addAttribute("contact", contact);
						return "normal/addContact";
						
					}else if(file.isEmpty()) {
						//if file is empty display the message
						System.out.println("no file uploaded");
						contact.setImage("default.jpg");
					}
					else{
						contact.setImage(file.getOriginalFilename());
						
						File saveFile = new ClassPathResource("static/img").getFile();
						
						Path path = Paths.get(saveFile.getAbsolutePath()+ File.separator + file.getOriginalFilename());
						
						Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
						
						}
					
					this.userRepository.save(user);
					session.setAttribute("message", new Message("Great! contact added sucessfully...","alert-success"));
					model.addAttribute("contact",new Contact());
					System.out.println(user);
					
					
		}catch(Exception e) {
			
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong, try again!!","alert-danger"));
			model.addAttribute("contact",contact);
		}
		
		return "normal/addContact";
	}
	
	
	
	//view all contacts controller
	@GetMapping("/viewAllContacts/{page}")
	public String allContacts(@PathVariable("page")Integer page, Principal principal,HttpSession session,Model model) {
		
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		int uid = user.getId();
		
		Pageable pageable = PageRequest.of(page, 7);
		
		Page<Contact> list = contactRepository.findPageableContactsById(uid,pageable);
		System.out.print(list);
		
		model.addAttribute("fiveContactList", list);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", list.getTotalPages());
		
		session.setAttribute("currentPg", page);
		
		
		return "normal/viewAllContacts";
	}
	
	
	
	//update contact controller
	@PostMapping("/updateContact/{cId}")
	public String UpdateContact(@PathVariable("cId")int id,Model model,Principal principal,HttpSession session) {
		
		
		Optional<Contact> contactOptional = contactRepository.findById(id);
		Contact contact = contactOptional.get();
		model.addAttribute("contact", contact);
		
		String pgno = session.getAttribute("currentPg").toString();
		
		
		model.addAttribute("currentPgno", pgno);
		
		return "normal/updateContact";
	}
	
	//processing updated contact
	@PostMapping("/processUpdateContact/{id}")
	public String processUpdatedContact(@ModelAttribute Contact updatedContact, @RequestParam("profileImage")MultipartFile file,@PathVariable("id")Integer id,HttpSession session) throws IOException
	{
		
		Optional<Contact> prevContactOptional = contactRepository.findById(id);
		Contact prevContact = prevContactOptional.get();
		 
		 updatedContact.setcId(prevContact.getcId());
		 updatedContact.setUser(prevContact.getUser());
		 if(!file.isEmpty()) {
			 
			//generating the absolute path of the img folder
			File saveFile = new ClassPathResource("/static/img").getFile();
			
			//Deleting old file from class path
			if(!prevContact.getImage().equals("default.jpg")) {
			Path oldFilePath = Paths.get(saveFile.getAbsolutePath()+ File.separator + prevContact.getImage());
			Files.delete(oldFilePath);
			}
			//copying new file to the class path
			Path path = Paths.get(saveFile.getAbsolutePath()+ File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			updatedContact.setImage(file.getOriginalFilename());
			
		 }else {
			 updatedContact.setImage(prevContact.getImage());
		 }
		 
		 
		 contactRepository.save(updatedContact);
		 
		
		String pgno = session.getAttribute("currentPg").toString();
		session.removeAttribute("currentPg");
		
		return "redirect:/user/viewAllContacts/"+pgno;
	}
	
	//delete contact controller
	@GetMapping("/deleteContact/{id}")
	public String delete(@PathVariable("id")Integer Id, Model model, Principal principal,HttpSession session) {
		Optional<Contact> contactOptional = contactRepository.findById(Id);
		Contact contact =  contactOptional.get();
		
		//bug fix 
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		
		if(user.getId() == contact.getUser().getId())
		{
		//we cannot delete single contact because we have used cascadetype-All
		//1: remove the cascade type(it will disturpt our persistance logic and mapping )
			//OR
		//2: set user mapped to contact to null(preferable for our project) 
		contactRepository.deleteContactById(Id);
		}
		
		//returning the same page by storing pg no in session
		String pgno = session.getAttribute("currentPg").toString();
		session.removeAttribute("currentPg");
		
		return "redirect:/user/viewAllContacts/"+pgno;
	}
	
	
	//view profile controller
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		int count = user.getContacts().size();
		System.out.println(count);
		
		
		model.addAttribute("user",user);
		model.addAttribute("TotalContacts", count);
		return "normal/userDashboard";
	}
	
	
	//update user
	@PostMapping("/updateUser")
	public String updateUser(Principal principal,Model model) {
		
		String username = principal.getName();
		User prevUser = userRepository.getUserByUserName(username);
		model.addAttribute("prevUser", prevUser);
		
		return "normal/updateUser";
	}
	
	//process updated user
	@PostMapping("/processUpdatedUser")
	public String processUpdatedUser(@ModelAttribute User updatedUser,@RequestParam("profileImage")MultipartFile file,Model model) throws IOException {
		
		File saveFile = new ClassPathResource("static/img").getFile();
		
		
		Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
		
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		updatedUser.setImageUrl(file.getOriginalFilename());
		userRepository.save(updatedUser);
		
		return "redirect:/user/profile";
	}
	
	@GetMapping("/settings")
	public String setting(){
		return "normal/settings";
	}
	
	@PostMapping("/changePassword")
	public String processSetting(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword")String newPassword, Principal principal, HttpSession session){
		
		System.out.println(principal.getName());
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		
		if(this.bcryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.bcryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(user);	
			session.setAttribute("message", new Message("your password is updated sucessfully !!", "alert-success"));
		}else {
			session.setAttribute("message", new Message("Enter your password correctly !!", "alert-danger"));
		}
		
		
		return "redirect:/user/settings";
	}
}
