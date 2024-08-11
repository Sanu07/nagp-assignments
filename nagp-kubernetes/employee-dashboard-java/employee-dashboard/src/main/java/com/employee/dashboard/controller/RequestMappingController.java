package com.employee.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.employee.dashboard.configuration.AppConstants;

@RestController
public class RequestMappingController {

	@GetMapping
	public ModelAndView redirectToDashboardPage() {
		return new ModelAndView("redirect:" + AppConstants.EMPLOYEE_SERVICE_GET_EMPLOYEES_URL);
	}
	
	@GetMapping("/health")
	public String imHealthy() {
		return "{healthy: true}";
	}
}
