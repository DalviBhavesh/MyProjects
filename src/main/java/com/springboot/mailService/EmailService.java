package com.springboot.mailService;


public interface EmailService {
	public void sendEmail(String subject,String message, String to);
}
