package com.nagarro.exceptions;
public class AuthenticationException extends RuntimeException {
	
	private static final long serialVersionUID = -397601889538702971L;

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}