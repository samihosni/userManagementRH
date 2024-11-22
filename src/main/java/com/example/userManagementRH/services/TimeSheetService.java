package com.example.userManagementRH.services;

import com.example.userManagementRH.entities.TimeSheet;
import com.example.userManagementRH.repositories.TimeSheetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSheetService {

    @Autowired

    private TimeSheetRepo timeSheetRepo;
    public TimeSheet createTimesheet(TimeSheet timeSheet) {
        return timeSheetRepo.save(timeSheet);
    }

    public List<TimeSheet> getAllTimesheets() {
        return timeSheetRepo.findAll();
    }

    public List<TimeSheet> getTimeSheetsByUserId(Long userId) {
        return timeSheetRepo.findByUserId(userId);
    }



    public TimeSheet updateTimesheet(Long id, TimeSheet updatedTimesheet) {
        return timeSheetRepo.findById(id).map(timesheet -> {
            timesheet.setHoursWorked(updatedTimesheet.getHoursWorked());
            timesheet.setDate(updatedTimesheet.getDate());
            return timeSheetRepo.save(timesheet);
        }).orElseThrow(() -> new RuntimeException("Timesheet not found"));
    }

    @PreAuthorize("hasRole('HR') || hasRole('ADMIN') ")
    public TimeSheet validateTimesheet(Long id) {
        TimeSheet timesheet = timeSheetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));
        timesheet.setValidated(true);
        return timeSheetRepo.save(timesheet);
    }

    public void deleteTimesheet(Long id) {
        timeSheetRepo.deleteById(id);
    }
}
