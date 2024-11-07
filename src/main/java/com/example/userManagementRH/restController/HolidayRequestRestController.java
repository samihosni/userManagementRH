package com.example.userManagementRH.restController;

import com.example.userManagementRH.entities.HolidayRequest;
import com.example.userManagementRH.services.HolidayRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/holidayRequest")
@RequiredArgsConstructor
public class HolidayRequestRestController {
    @Autowired
    private HolidayRequestService holidayRequestService;

    @PostMapping("/add")
    public ResponseEntity<HolidayRequest> createLeaveRequest(
            @RequestParam("holidayRequest") String holidayRequestJson,
            @RequestParam("file") MultipartFile file) throws IOException {
        // Convertissez holidayRequestJson en un objet HolidayRequest
        // Vous pouvez utiliser un convertisseur JSON, comme Jackson ou Gson
        HolidayRequest holidayRequest = new ObjectMapper().readValue(holidayRequestJson, HolidayRequest.class);

        // Appelez la méthode du service pour traiter la demande de congé et le fichier
        HolidayRequest createdRequest = holidayRequestService.createLeaveRequest(holidayRequest, file);

        return ResponseEntity.ok(createdRequest);
    }

    @GetMapping("/all")
    public List<HolidayRequest> getAllLeaveRequests() {
        return holidayRequestService.getAllLeaveRequests();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HolidayRequest> updateLeaveRequest(@PathVariable Long id, @RequestBody HolidayRequest updatedLeave) {
        return ResponseEntity.ok(holidayRequestService.updateLeaveRequest(id, updatedLeave));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<HolidayRequest> approveLeaveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(holidayRequestService.approveLeaveRequest(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        holidayRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }
}
