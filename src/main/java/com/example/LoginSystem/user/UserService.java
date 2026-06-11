package com.example.LoginSystem.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void register(String email, String password) {
		String normalizedEmail = normalizeEmail(email);
		List<String> errors = validateRegistration(normalizedEmail, password);
		if (!errors.isEmpty()) {
			throw new InvalidRegistrationException(String.join(" ", errors));
		}
		if (userRepository.existsByEmail(normalizedEmail)) {
			throw new DuplicateEmailException("Email is already registered.");
		}

		userRepository.save(new AppUser(normalizedEmail, passwordEncoder.encode(password)));
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String normalizedEmail = normalizeEmail(email);
		AppUser user = userRepository.findByEmail(normalizedEmail)
			.orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));

		return User.withUsername(user.getEmail())
			.password(user.getPassword())
			.roles("USER")
			.build();
	}

	private List<String> validateRegistration(String email, String password) {
		List<String> errors = new ArrayList<>();
		if (email == null || email.isBlank()) {
			errors.add("Email is required.");
		}
		else if (!EMAIL_PATTERN.matcher(email).matches()) {
			errors.add("Email must be valid.");
		}
		if (password == null || password.isBlank()) {
			errors.add("Password is required.");
		}
		return errors;
	}

	private String normalizeEmail(String email) {
		if (email == null) {
			return null;
		}
		return email.trim().toLowerCase(Locale.ROOT);
	}
}
