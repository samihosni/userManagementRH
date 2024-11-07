package com.example.userManagementRH.repositories;

import com.example.userManagementRH.entities.HolidayRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface HolidayRequestRepo extends JpaRepository<HolidayRequest, Long> {
    List<HolidayRequest> findByUserId(Long userId);
}
