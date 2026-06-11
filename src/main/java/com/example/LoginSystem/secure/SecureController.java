package com.example.LoginSystem.secure;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

	@GetMapping("/secure")
	public String securePage() {
		return "This is a secure page.";
	}
}
