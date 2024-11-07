package com.example.userManagementRH.services;

import com.example.userManagementRH.authentication.AuthenticationService;
import com.example.userManagementRH.entities.Notifications;
import com.example.userManagementRH.entities.User;
import com.example.userManagementRH.repositories.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;
    private JavaMailSender mailSender;

    @Autowired
    private AuthenticationService userService; // Pour récupérer les utilisateurs (assurez-vous d'avoir un UserService)

    // Méthode pour créer une notification et envoyer un e-mail
    public Notifications createNotification(Notifications notifications) {
        Notifications savedNotification = notificationRepo.save(notifications);

        // Envoi de la notification par e-mail
        sendEmailNotification(savedNotification);

        return savedNotification;
    }

    // Récupérer toutes les notifications
    public List<Notifications> getAllNotifications() {
        return notificationRepo.findAll();
    }

    // Marquer une notification comme lue
    public void markNotificationAsRead(Long id) {
        notificationRepo.findById(id).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepo.save(notification);
        });
    }

    // Envoi d'un e-mail de notification général
    private void sendEmailNotification(Notifications notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getUser().getEmail()); // Assurez-vous que le champ email existe dans User
        message.setSubject("Nouvelle notification");
        message.setText(notification.getMessage() + "\nEnvoyé le: " + notification.getTimestamp());

        mailSender.send(message);
    }

    // Notification pour une demande de congé
    public void sendLeaveRequestNotification(User user, String leaveDetails) {
        Notifications notification = new Notifications();
        notification.setUser(user);
        notification.setMessage("Nouvelle demande de congé: " + leaveDetails);
        notification.setTimestamp(LocalDateTime.now());

        createNotification(notification); // Enregistre et envoie l'email
    }

    // Notification pour une évaluation de performance
    public void sendEvaluationNotification(User user, String evaluationResult) {
        Notifications notification = new Notifications();
        notification.setUser(user);
        notification.setMessage("Résultat d'évaluation: " + evaluationResult);
        notification.setTimestamp(LocalDateTime.now());

        createNotification(notification);
    }

    // Rappel quotidien automatique pour tous les utilisateurs
    @Scheduled(cron = "0 0 9 * * ?") // Exécution chaque jour à 9h
    public void sendDailyReminders() {
        List<User> users = userService.getAllUsers(); // Méthode pour obtenir tous les utilisateurs

        for (User user : users) {
            Notifications reminder = new Notifications();
            reminder.setUser(user);
            reminder.setMessage("Rappel quotidien: N'oubliez pas de consulter vos notifications.");
            reminder.setTimestamp(LocalDateTime.now());

            createNotification(reminder); // Enregistre et envoie l'email
        }
    }
}
