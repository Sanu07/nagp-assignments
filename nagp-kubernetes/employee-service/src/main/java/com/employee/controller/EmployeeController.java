package com.employee.controller;

import static com.employee.config.AppConstants.GET_EMPLOYEES_URL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.exceptions.AppException;
import com.employee.model.Employee;
import com.employee.service.impl.EmployeeServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(GET_EMPLOYEES_URL)
@Slf4j
public class EmployeeController {

	@Autowired
	private EmployeeServiceImpl employeeService;

	@GetMapping
	public ResponseEntity<List<Employee>> getEmployees() {
		List<Employee> employeesList = null;
		try {
			employeesList = employeeService.getEmployees();
		} catch (Exception e) {
			log.error("[EmployeeController][getEmployees] problem in retrieving employee from Database ", e);
			throw new AppException(e);
		}
		return ResponseEntity.ok(employeesList);
	}
	
	@GetMapping("/health")
	public String imHealthy() {
		return "{healthy: true}";
	}
}
