package com.mimozalab.innbalance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mimozalab.innbalance.exception.ResourceNotFoundException;
import com.mimozalab.innbalance.model.User;
import com.mimozalab.innbalance.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updated) {
        User existing = getById(id);
        if (updated.getUsername() != null) existing.setUsername(updated.getUsername());
        if (updated.getPassword() != null) existing.setPassword(updated.getPassword());
        // keep createdAt unchanged unless explicitly set
        if (updated.getCreatedAt() != null) existing.setCreatedAt(updated.getCreatedAt());
        return userRepository.save(existing);
    }

    public void deleteById(Long id) {
        User existing = getById(id);
        userRepository.delete(existing);
    }

    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}