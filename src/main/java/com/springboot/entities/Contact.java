package com.springboot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="CONTACT")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	@Size(min=2,max=40,message="*min 2 and max 40 characters are allowed !")
	@NotBlank(message = "*enter name for this contact")
	private String name;
	
	@Size(min=2, max=40,message="*min 2 and max 40 characters are allowed !")
	private String nickName;
	
	@Pattern(regexp = "^[a-zA-Z ]*$", message="*numbers or special characters are not allowed (for security reasons)")
	private String work;
	
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message="*enter a valid email Id")
	private String email;
	
	@NotBlank(message="Enter a phone number")
	@Pattern(regexp = "^[0-9]*$", message="*only 0-9 numbers allowed ")
	@Size(min=10, max=10, message="*enter a valid phone number")
	private String phone;
	
	private String image;
	
	@Column(length=1000)
	@Size(max=1000, message="*discription must be under 1000 words")
	private String description;
	
	@ManyToOne
	private User user;
	
	
	
	

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Contact(int cId, String name, String nickName, String work, String email, String phone, String image,
			String description, User user) {
		super();
		this.cId = cId;
		this.name = name;
		this.nickName = nickName;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
		this.user = user;
	}

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
