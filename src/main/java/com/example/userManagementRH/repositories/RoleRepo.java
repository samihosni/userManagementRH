package com.example.userManagementRH.repositories;

import com.example.userManagementRH.entities.ERole;
import com.example.userManagementRH.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);



}
