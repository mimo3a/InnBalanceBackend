package com.mimozalab.innbalance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mimozalab.innbalance.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}
