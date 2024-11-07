package com.example.userManagementRH.repositories;

import com.example.userManagementRH.entities.Notifications;
import com.example.userManagementRH.entities.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notifications, Long> {
    List<Notifications> findByUserIdAndIsReadFalse(Long userId);
}
