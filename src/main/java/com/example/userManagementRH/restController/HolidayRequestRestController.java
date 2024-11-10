package com.example.userManagementRH.restController;

import com.example.userManagementRH.entities.HolidayRequest;
import com.example.userManagementRH.services.HolidayRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    /**
     * Endpoint to create a new holiday request with a file upload.
     */
    @PostMapping("/add")
    public ResponseEntity<HolidayRequest> createLeaveRequest(
            @RequestParam("holidayRequest") String holidayRequestJson,  // Expecting JSON string
            @RequestParam("file") MultipartFile file) throws IOException {  // Expecting a file

        // Convert the JSON string to a HolidayRequest object
        HolidayRequest holidayRequest = new ObjectMapper().readValue(holidayRequestJson, HolidayRequest.class);

        // Pass the HolidayRequest and file to the service to handle the business logic
        HolidayRequest createdRequest = holidayRequestService.createLeaveRequest(holidayRequest, file);

        // Return the created request as a response
        return ResponseEntity.ok(createdRequest);
    }

    /**
     * Endpoint to get all holiday requests.
     */
    @GetMapping("/all")
    public List<HolidayRequest> getAllLeaveRequests() {
        return holidayRequestService.getAllLeaveRequests();
    }

    /**
     * Endpoint to update a holiday request.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<HolidayRequest> updateLeaveRequest(
            @PathVariable Long id, @RequestBody HolidayRequest updatedLeave) {
        return ResponseEntity.ok(holidayRequestService.updateLeaveRequest(id, updatedLeave));
    }

    /**
     * Endpoint to approve a holiday request.
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<HolidayRequest> approveLeaveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(holidayRequestService.approveLeaveRequest(id));
    }

    /**
     * Endpoint to delete a holiday request.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        holidayRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }
}
