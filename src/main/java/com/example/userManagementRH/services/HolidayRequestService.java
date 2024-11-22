package com.example.userManagementRH.services;

import com.example.userManagementRH.entities.HolidayRequest;
import com.example.userManagementRH.repositories.HolidayRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class HolidayRequestService {

    @Autowired
    private HolidayRequestRepo holidayRequestRepo;
    public HolidayRequest createLeaveRequest(HolidayRequest holidayRequest, MultipartFile file) throws IOException {
        // Gérer l'enregistrement du fichier
        String fileName = file.getOriginalFilename();
        String filePath = "C:/Users/21650/Documents/tekup" + fileName;

        // Enregistrez le fichier sur le système de fichiers
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());

        holidayRequest.setFilePath(filePath);

        return holidayRequestRepo.save(holidayRequest);
    }

    public List<HolidayRequest> getAllLeaveRequests() {
        return holidayRequestRepo.findAll();
    }

    public HolidayRequest updateLeaveRequest(Long id, HolidayRequest updatedLeave) {
        return holidayRequestRepo.findById(id).map(leave -> {
            leave.setStartDate(updatedLeave.getStartDate());
            leave.setEndDate(updatedLeave.getEndDate());
            leave.setStatus(updatedLeave.getStatus());
            return holidayRequestRepo.save(leave);
        }).orElseThrow(() -> new RuntimeException("Leave request not found"));
    }

    @PreAuthorize("hasRole('HR')")
    public HolidayRequest approveLeaveRequest(Long id) {
       HolidayRequest leaveRequest = holidayRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        leaveRequest.setStatus("APPROVED");
        return holidayRequestRepo.save(leaveRequest);
    }

    public void deleteLeaveRequest(Long id) {
        holidayRequestRepo.deleteById(id);
    }

}
