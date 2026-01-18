package com.mimozalab.innbalance.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mimozalab.innbalance.dto.AuthResponse;
import com.mimozalab.innbalance.dto.LoginRequest;
import com.mimozalab.innbalance.dto.SignUpRequest;
import com.mimozalab.innbalance.exception.ResourceNotFoundException;
import com.mimozalab.innbalance.model.User;
import com.mimozalab.innbalance.repository.UserRepository;
import com.mimozalab.innbalance.security.JwtTokenProvider;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    // Регистрация нового пользователя
    public AuthResponse registerUser(SignUpRequest request) {
        // Проверка: пользователь уже существует?
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Создание нового пользователя
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        // Генерация JWT токена
        String token = jwtTokenProvider.generateToken(user.getUsername());
        
        return new AuthResponse(token, user.getUsername());
    }
    
    // Вход пользователя
    public AuthResponse authenticateUser(LoginRequest request) {
        // Найти пользователя
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        // Проверить пароль
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        // Генерация JWT токена
        String token = jwtTokenProvider.generateToken(user.getUsername());
        
        return new AuthResponse(token, user.getUsername());
    }
    
    // Выход (опционально - можно добавить blacklist для токенов)
    public void logout(String token) {
        // В простой версии JWT токен истекает сам
        // Для blacklist можно добавить Redis или БД
        // tokenBlacklistService.addToBlacklist(token);
    }
    
    // Удаление аккаунта
    public void deleteAccount(String token) {
        if (token == null) {
            throw new RuntimeException("Token is required");
        }
        // Remove optional "Bearer " prefix
        String rawToken = token.replaceFirst("(?i)^Bearer\\s+", "");
        String username = jwtTokenProvider.getUsernameFromToken(rawToken);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }
}