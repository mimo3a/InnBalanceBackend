package com.mimozalab.innbalance.controller;

import com.mimozalab.innbalance.dto.ChangePasswordRequest;
import com.mimozalab.innbalance.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal(); // set in JwtAuthFilter
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}