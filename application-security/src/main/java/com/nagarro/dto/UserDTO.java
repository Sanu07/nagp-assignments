package com.nagarro.dto;

import lombok.Data;

@Data
public class UserDTO {

	private String fullName;
	private String password;
	private String email;
	private String phone;
	private String imageFilePath;
	
}