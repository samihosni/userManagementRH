package com.example.userManagementRH.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
