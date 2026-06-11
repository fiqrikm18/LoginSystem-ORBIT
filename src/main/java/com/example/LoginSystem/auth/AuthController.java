package com.example.LoginSystem.auth;

import com.example.LoginSystem.user.DuplicateEmailException;
import com.example.LoginSystem.user.InvalidRegistrationException;
import com.example.LoginSystem.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String register(@RequestParam(required = false) String email,
			@RequestParam(required = false) String password) {
		userService.register(email, password);
		return "User registered successfully.";
	}

	@ExceptionHandler(InvalidRegistrationException.class)
	public ResponseEntity<String> handleInvalidRegistration(InvalidRegistrationException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
	}
}
