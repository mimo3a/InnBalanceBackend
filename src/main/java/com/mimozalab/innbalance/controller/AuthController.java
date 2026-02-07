package com.mimozalab.innbalance.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mimozalab.innbalance.dto.AuthResponse;
import com.mimozalab.innbalance.dto.LoginRequest;
import com.mimozalab.innbalance.dto.SignUpRequest;
import com.mimozalab.innbalance.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")   // 👈 ЭТО ОБЯЗАТЕЛЬНО
    public AuthResponse signup(@RequestBody SignUpRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")   // 👈 ЭТО ОБЯЗАТЕЛЬНО
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
