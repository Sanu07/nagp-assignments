package com.nagarro.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.dto.UserDTO;
import com.nagarro.entity.User;
import com.nagarro.exceptions.AuthenticationException;
import com.nagarro.model.TokenRequest;
import com.nagarro.model.TokenResponse;
import com.nagarro.service.impl.UserServiceImpl;
import com.nagarro.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuthController {

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private UserServiceImpl userDetailsService;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(userDetailsService.getUsers());
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody TokenRequest authenticationRequest)
			throws AuthenticationException {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = tokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new TokenResponse(token));

	}

	@GetMapping("/refresh")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = tokenUtil.getUsernameFromToken(token);
		userDetailsService.loadUserByUsername(username);

		if (tokenUtil.canTokenBeRefreshed(token).booleanValue()) {
			String refreshedToken = tokenUtil.refreshToken(token);
			return ResponseEntity.ok(new TokenResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
		User user = new User();
		BeanUtils.copyProperties(userDTO, user);
		user.setUserName(userDTO.getFullName().substring(0, 2).toUpperCase() + userDTO.getPhone());
		user.setImageFilePath("default/s3/location");
		User savedUser = userDetailsService.save(user);
		log.info("User created with id [{}]", savedUser.getId());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/access")
	public ResponseEntity<String> getAccess(@RequestBody String name) {
		log.info("Hey {}!!! You got access to checkout any order");
		return ResponseEntity.ok("Hey " + name + "!!! You got access to checkout any order");
	}
	
	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("INVALID_CREDENTIALS", e);
		}
	}
}