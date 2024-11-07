package com.example.userManagementRH.restController;

import com.example.userManagementRH.entities.TimeSheet;
import com.example.userManagementRH.services.TimeSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeSheet")
@RequiredArgsConstructor
public class TimeSheetRestController {

    @Autowired
    private TimeSheetService timeSheetService;

    @PostMapping("/add")
    public ResponseEntity<TimeSheet> createTimesheet(@RequestBody TimeSheet timesheet) {
        return ResponseEntity.ok(timeSheetService.createTimesheet(timesheet));
    }

    @GetMapping("/all")
    public List<TimeSheet> getAllTimesheets() {
        return timeSheetService.getAllTimesheets();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TimeSheet> updateTimesheet(@PathVariable Long id, @RequestBody TimeSheet updatedTimesheet) {
        return ResponseEntity.ok(timeSheetService.updateTimesheet(id, updatedTimesheet));
    }

    @PutMapping("/{id}/validate")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<TimeSheet> validateTimesheet(@PathVariable Long id) {
        return ResponseEntity.ok(timeSheetService.validateTimesheet(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTimesheet(@PathVariable Long id) {
        timeSheetService.deleteTimesheet(id);
        return ResponseEntity.noContent().build();
    }
}
