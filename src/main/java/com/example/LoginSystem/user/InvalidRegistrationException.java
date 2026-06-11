package com.example.LoginSystem.user;

public class InvalidRegistrationException extends RuntimeException {

	public InvalidRegistrationException(String message) {
		super(message);
	}
}
