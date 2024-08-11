package com.employee.dashboard.configuration;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class ApplicationConfiguration {

	@Value("${EMPLOYEE_SERVICE_URI:http://localhost}")
	private String employeeServiceBaseUrl;

	@Value("${employee.service.port:9000}")
	private String employeeServicePort;

	@Value("${restTemplate.connectionTimeout}")
	private Integer restConnectionTimeout;
	
	@Value("${kubernetes.host.envVariables}")
	private List<String> kubeHostEnvVariables;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder()
				.setConnectTimeout(Duration.ofMillis(restConnectionTimeout))
				.setReadTimeout(Duration.ofMillis(restConnectionTimeout))
				.build();
	}

}
