package com.springboot.mailService;


import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
	  public void sendEmail(String subject,String message, String to){

	        String from = "dalvibhavesh007@gmail.com";
	        String host = "smtp.gmail.com";

	        Properties properties = System.getProperties();
	        System.out.println("PROPERTIES: "+properties);


	        properties.put("mail.smtp.host",host);
	        properties.put("mail.smtp.port","465");
	        properties.put("mail.smtp.ssl.enable","true");
	        properties.put("mail.smtp.auth","true");

	        Session mailSession = Session.getInstance(properties, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(from,"uinu bypr ceex ypnb");
	            }
	        });

	        mailSession.setDebug(true);



	        try {
	            MimeMessage m = new MimeMessage(mailSession);
	            m.setFrom(from);

	            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	            m.setSubject(subject);

	            m.setText(message);

	            Transport.send(m);

	            System.out.println("Sent sucesfully...........");

	        }catch(Exception e){
	            e.printStackTrace();
	        }


	    }
}
