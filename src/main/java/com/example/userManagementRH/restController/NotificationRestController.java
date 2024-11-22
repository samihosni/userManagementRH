package com.example.userManagementRH.restController;

import com.example.userManagementRH.entities.Notifications;
import com.example.userManagementRH.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationRestController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/add")
    public ResponseEntity<Notifications> createNotification(@RequestBody Notifications notification) {
        return ResponseEntity.ok(notificationService.createNotification(notification));
    }

    @GetMapping("/all")
    public List<Notifications> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
