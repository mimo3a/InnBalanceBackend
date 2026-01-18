package com.mimozalab.innbalance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mimozalab.innbalance.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Поиск пользователя по username
    Optional<User> findByUsername(String username);
    
    // Проверка существования пользователя
    Boolean existsByUsername(String username);
    
    // Поиск по email (если используется)
    Optional<User> findByEmail(String email);
    
    // Удаление по username
    void deleteByUsername(String username);
}