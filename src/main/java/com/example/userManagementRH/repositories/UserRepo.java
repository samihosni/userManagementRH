package com.example.userManagementRH.repositories;

import com.example.userManagementRH.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
