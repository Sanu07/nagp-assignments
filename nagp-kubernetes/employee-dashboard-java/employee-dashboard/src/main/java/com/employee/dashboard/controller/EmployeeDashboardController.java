package com.employee.dashboard.controller;

import static com.employee.dashboard.configuration.AppConstants.COLON_DELIMITER;
import static com.employee.dashboard.configuration.AppConstants.EMPLOYEE_SERVICE_GET_EMPLOYEES_URL;
import static com.employee.dashboard.configuration.AppConstants.ERROR_PAGE;
import static com.employee.dashboard.configuration.AppConstants.INDEX_PAGE;
import static com.employee.dashboard.configuration.AppConstants.SLASH_DELIMITER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.employee.dashboard.configuration.ApplicationConfiguration;
import com.employee.dashboard.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(EMPLOYEE_SERVICE_GET_EMPLOYEES_URL)
@Slf4j
public class EmployeeDashboardController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ApplicationConfiguration config;

	@Autowired
	private Environment env;

	@Retry(name = "all-employees-api", fallbackMethod = "defaultResponseFB")
	@GetMapping
	public String getEmployeeData(Model model) {
		// the URL is configured with the kubernetes service name
		// 'employee-service-mysql' when deployed to kubernetes
		// and to 'localhost' when running locally. The default port is configured to
		// 9000
		String URL = String.join(SLASH_DELIMITER,
				String.join(COLON_DELIMITER, config.getEmployeeServiceBaseUrl(), config.getEmployeeServicePort()),
				EMPLOYEE_SERVICE_GET_EMPLOYEES_URL);
		log.info("getting result from [{}]", URL);
		List<Employee> employees = new ArrayList<>();
		try {
			ResponseEntity<String> entity = restTemplate.getForEntity(URL, String.class);
			employees = mapper.readValue(entity.getBody().getBytes(), new TypeReference<List<Employee>>() {
			});
		} catch (IOException e) {
			log.error(
					"[EmployeeDashboardController][getEmployeeData] response entity could not be casted to List<Employee> ",
					e);
			return ERROR_PAGE;
		} catch (Exception e) {
			log.error(
					"[EmployeeDashboardController][getEmployeeData] error in getting response from employee-service-mysql");
			model.addAttribute("error", true);
			model.addAttribute("errorMessage", "There is some error. Please try later or contact the administrator.");
			throw e;
		}
		if (!employees.isEmpty()) {
			log.info("size of employee list is {} ", employees.size());
		} else {
			log.info("no employee could be found");
		}
		model.addAttribute("hostEnvVariablesMap", getHostEnvironmentVariables());
		model.addAttribute("employeesList", employees);
		return INDEX_PAGE;
	}

	public String defaultResponseFB(Throwable t) {
		log.error("[EmployeeDashboardController][defaultResponseFB] Could not get response even after retrying ", t);
		return INDEX_PAGE;
	}

	/**
	 * this method fetches all the host environment variables and creates a map out
	 * of it
	 * 
	 * @return host environment variables
	 */
	private Map<String, String> getHostEnvironmentVariables() {
		final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
		Map<String, String> map = new HashMap<>();
		StreamSupport.stream(sources.spliterator(), false).filter(ps -> ps instanceof EnumerablePropertySource)
				.map(ps -> ((EnumerablePropertySource) ps).getPropertyNames()).flatMap(Arrays::stream).distinct()
				.forEach(prop -> {
					Object resolved = env.getProperty(prop, Object.class);
					if (resolved instanceof String) {
						log.info("{} - {}", prop, env.getProperty(prop));
						if (config.getKubeHostEnvVariables().contains(prop)) {
							map.put(prop, env.getProperty(prop));
						}
					} else {
						log.info("{} - {}", prop, "NON-STRING-VALUE");
					}
				});
		return map;
	}
}
