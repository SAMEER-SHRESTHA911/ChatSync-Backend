package com.sameer.ChatApp.controller;

import com.sameer.ChatApp.config.JwtUtil;
import com.sameer.ChatApp.dto.LoginResponse;
import com.sameer.ChatApp.model.User;
import com.sameer.ChatApp.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotBlank;

import java.net.http.HttpResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {
    //    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public static class RegisterRequest {

        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Email is required")
        private String email;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class AuthResponse {
        private final String message;
        private final String token;
        private final int status;

        public AuthResponse(String message, String token, int status) {
            this.message = message;
            this.token = token;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }

        public int getStatus(){
            return status;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
//        logger.info("Received registration request for username: {}", request.getUsername());
        try {
            userService.registerUser(request.getUsername(),request.getPassword(), request.getEmail());
//            logger.info("User registered successfully: {}", request.getUsername());
            return ResponseEntity.ok(new AuthResponse("User registered successfully", null, HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
//            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponse(e.getMessage(), null, HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
//        logger.info("Received login request for username: {}", request.getUsername());
        try {
            User user = userService.authenticateByEmail(request.getEmail(), request.getPassword());
            String token = jwtUtil.generateToken(user);
//            logger.info("Login successful for user: {}", request.getUsername());
            return ResponseEntity.ok(new LoginResponse("Login Successful", token, HttpStatus.OK.value(), user.getId()));
        } catch (UserService.AuthenticationException e) {
//            logger.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(e.getMessage(), null, HttpStatus.UNAUTHORIZED.value(), null));
        }
    }
}

