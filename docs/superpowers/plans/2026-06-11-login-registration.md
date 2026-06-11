# Login Registration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a Spring Boot website login and registration system using Java 17, Spring Security, H2, MVC layering, and BCrypt password hashing.

**Architecture:** Use session-based Spring Security authentication with email as the username parameter. Keep controller, service, repository, entity, and security configuration responsibilities separate. Return plain, human-readable text for registration and login outcomes, and expose one authenticated-only `/secure` page.

**Tech Stack:** Java 17, Spring Boot, Spring Web MVC, Spring Security, Spring Data JPA, H2, JUnit, MockMvc.

---

### Task 1: Auth Behavior Tests

**Files:**
- Create: `src/test/java/com/example/LoginSystem/AuthIntegrationTests.java`

- [ ] Write MockMvc tests for successful registration, duplicate email, invalid input, secure-page redirect before login, successful login, and invalid login.
- [ ] Run `./gradlew test --tests com.example.LoginSystem.AuthIntegrationTests` and verify tests fail because auth endpoints do not exist yet.

### Task 2: Persistence And Registration

**Files:**
- Create: `src/main/java/com/example/LoginSystem/user/AppUser.java`
- Create: `src/main/java/com/example/LoginSystem/user/UserRepository.java`
- Create: `src/main/java/com/example/LoginSystem/user/UserService.java`
- Create: `src/main/java/com/example/LoginSystem/auth/AuthController.java`

- [ ] Implement `AppUser` with unique email and BCrypt-hashed password.
- [ ] Implement registration validation for required email, valid email shape, required password, and duplicate email.
- [ ] Run the registration-related tests and verify they pass.

### Task 3: Spring Security Login And Secure Page

**Files:**
- Create: `src/main/java/com/example/LoginSystem/config/SecurityConfig.java`
- Create: `src/main/java/com/example/LoginSystem/secure/SecureController.java`
- Modify: `src/main/java/com/example/LoginSystem/user/UserService.java`

- [ ] Configure Spring Security to use `email` and `password` form parameters at `POST /login`.
- [ ] Return `Login successful` on login success and `Invalid email or password.` on login failure.
- [ ] Protect `/secure` and return a simple authenticated page.
- [ ] Run the full test suite and verify everything passes.

### Task 4: H2 Configuration

**Files:**
- Modify: `src/main/resources/application.properties`

- [ ] Configure in-memory H2, JPA schema creation, H2 console, and SQL output settings.
- [ ] Run `./gradlew test` and verify all tests pass.
