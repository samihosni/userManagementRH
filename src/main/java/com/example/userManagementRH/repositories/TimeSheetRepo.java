package com.example.userManagementRH.repositories;

import com.example.userManagementRH.entities.Evaluation;
import com.example.userManagementRH.entities.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeSheetRepo extends JpaRepository<TimeSheet, Long> {
    List<TimeSheet> findByUserIdAndDate(Long userId, LocalDate date);
    List<TimeSheet> findByUserId(Long userId);

}
