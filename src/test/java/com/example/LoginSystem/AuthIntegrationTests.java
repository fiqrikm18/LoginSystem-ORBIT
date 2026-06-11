package com.example.LoginSystem;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void registersUserWithFormEncodedEmailAndPassword() throws Exception {
		mockMvc.perform(post("/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "register@example.com")
				.param("password", "mypassword"))
			.andExpect(status().isOk())
			.andExpect(content().string("User registered successfully."));
	}

	@Test
	void rejectsDuplicateEmailWithHumanReadableMessage() throws Exception {
		register("duplicate@example.com", "mypassword");

		mockMvc.perform(post("/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "duplicate@example.com")
				.param("password", "otherpassword"))
			.andExpect(status().isConflict())
			.andExpect(content().string("Email is already registered."));
	}

	@Test
	void rejectsInvalidRegistrationInputWithHumanReadableMessage() throws Exception {
		mockMvc.perform(post("/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "not-an-email")
				.param("password", ""))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("Email must be valid.")))
			.andExpect(content().string(containsString("Password is required.")));
	}

	@Test
	void redirectsUnauthenticatedUserAwayFromSecurePage() throws Exception {
		mockMvc.perform(get("/secure"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));
	}

	@Test
	void logsInRegisteredUserWithEmailAndPassword() throws Exception {
		register("login@example.com", "mypassword");

		mockMvc.perform(post("/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "login@example.com")
				.param("password", "mypassword"))
			.andExpect(status().isOk())
			.andExpect(content().string("Login successful"));
	}

	@Test
	void rejectsInvalidLoginWithHumanReadableMessage() throws Exception {
		register("invalid-login@example.com", "mypassword");

		mockMvc.perform(post("/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "invalid-login@example.com")
				.param("password", "wrongpassword"))
			.andExpect(status().isUnauthorized())
			.andExpect(content().string("Invalid email or password."));
	}

	private void register(String email, String password) throws Exception {
		mockMvc.perform(post("/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", email)
				.param("password", password))
			.andExpect(status().isOk());
	}
}
