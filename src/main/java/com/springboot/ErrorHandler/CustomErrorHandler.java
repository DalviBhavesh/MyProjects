package com.springboot.ErrorHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice 	
public class CustomErrorHandler {
	
	@ExceptionHandler(value = Exception.class)
	public String generalException() {
		return "redirect:/user/home";
	}
}
