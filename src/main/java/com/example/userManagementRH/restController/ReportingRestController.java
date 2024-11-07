package com.example.userManagementRH.restController;

import com.example.userManagementRH.services.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/reporting")
@RequiredArgsConstructor
public class ReportingRestController {
    @Autowired
    private ReportingService reportingService;

    @GetMapping("/leave-count")
    public ResponseEntity<Map<String, Long>> getLeaveCountPerEmployee() {
        return ResponseEntity.ok(reportingService.getLeaveCountPerEmployee());
    }

    @GetMapping("/average-performance")
    public ResponseEntity<Map<String, Double>> getAveragePerformanceScores() {
        return ResponseEntity.ok(reportingService.getAveragePerformanceScores());
    }

    @GetMapping("/hours-worked")
    public ResponseEntity<Map<String, Integer>> getTotalHoursWorked() {
        return ResponseEntity.ok(reportingService.getTotalHoursWorked());
    }


}
