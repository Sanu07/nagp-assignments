package com.nagarro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MappingController {

	@GetMapping("/register-page")
	public String register() {
		return "register.html";
	}
	
	@GetMapping("/authenticate-page")
	public String authenticate() {
		return "login.html";
	}
	
	@GetMapping("/checkout-page")
	public String checkout() {
		return "checkout-legal.html";
	}
}
