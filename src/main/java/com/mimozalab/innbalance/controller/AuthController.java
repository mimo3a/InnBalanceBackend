package com.mimozalab.innbalance.controller;

// ...existing code...
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mimozalab.innbalance.dto.AuthResponse;
import com.mimozalab.innbalance.dto.LoginRequest;
// ...existing code...
import com.mimozalab.innbalance.dto.SignUpRequest;
import com.mimozalab.innbalance.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
	@PostMapping("/login")
public AuthResponse login(@RequestBody LoginRequest request) {

    // временно, без настоящей проверки
    if (!"test@test.com".equals(request.getEmail())
        || !"123456".equals(request.getPassword())) {
        throw new RuntimeException("Invalid credentials");
    }

    return new AuthResponse(
        "fake-jwt-token-123",
        request.getEmail()
    );
}

}
