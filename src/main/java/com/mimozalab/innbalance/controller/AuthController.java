package com.mimozalab.innbalance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mimozalab.innbalance.dto.AuthResponse;
import com.mimozalab.innbalance.dto.LoginRequest;
import com.mimozalab.innbalance.dto.SignUpRequest;
import com.mimozalab.innbalance.service.AuthService;

@RestController
	@RequestMapping("/api/auth")
	public class AuthController {
	    
	    @Autowired
	    private AuthService authService;
	    
	    // POST /api/auth/signup
	    @PostMapping("/signup")
	    public ResponseEntity<AuthResponse> signup(@RequestBody SignUpRequest request) {
	        AuthResponse response = authService.registerUser(request);
	        return ResponseEntity.ok(response);
	    }
	    
	    // POST /api/auth/login
	    @PostMapping("/login")
	    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
	        AuthResponse response = authService.authenticateUser(request);
	        return ResponseEntity.ok(response);
	    }
	    
	    // POST /api/auth/logout
	    @PostMapping("/logout")
	    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
	        authService.logout(token);
	        return ResponseEntity.ok("Logged out successfully");
	    }
	

}
