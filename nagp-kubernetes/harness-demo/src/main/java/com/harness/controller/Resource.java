package com.harness.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
public class Resource {

    @Autowired
    private Environment env;

    @GetMapping("values")
    public Map<String, String> getValue() {
        return getHostEnvironmentVariables();
    }

    @GetMapping("healthCheck")
    public String healthcheck() {
        return "OK";
    }

    private static final List<String> HOST_VARIABLES = Arrays.asList("HOSTNAME","EMPLOYEE_SERVICE_URI");

    private Map<String, String> getHostEnvironmentVariables() {
        final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
        Map<String, String> map = new HashMap<>();
        StreamSupport.stream(sources.spliterator(), false).filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames()).flatMap(Arrays::stream).distinct()
                .forEach(prop -> {
                    Object resolved = env.getProperty(prop, Object.class);
                    if (resolved instanceof String) {
                        log.info("{} - {}", prop, env.getProperty(prop));
                        map.put(prop, env.getProperty(prop));
                    } else {
                        log.info("{} - {}", prop, "NON-STRING-VALUE");
                    }
                });
        return map;
    }
}
