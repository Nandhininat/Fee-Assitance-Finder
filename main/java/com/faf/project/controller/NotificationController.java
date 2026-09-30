package com.faf.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.faf.project.model.Notification;
import com.faf.project.service.NotificationService;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    // ==========================================
    // CREATE NOTIFICATION
    // ==========================================
    @PostMapping("/notification")
    public Notification saveNotification(
            @RequestBody Notification notification) {

        return notificationService.saveNotification(notification);
    }


    // ==========================================
    // GET ALL NOTIFICATIONS
    // ==========================================
    @GetMapping("/notification")
    public List<Notification> getAllNotifications() {

        return notificationService.getAllNotifications();
    }


    // ==========================================
    // GET NOTIFICATION BY ID
    // ==========================================
    @GetMapping("/notification/{notificationId}")
    public Notification getNotificationById(
            @PathVariable Long notificationId) {

        return notificationService
                .getNotificationById(notificationId);
    }


    // ==========================================
    // GET NOTIFICATIONS BY STUDENT
    // ==========================================
    @GetMapping("/notification/student/{studentId}")
    public List<Notification> getNotificationsByStudentId(
            @PathVariable Long studentId) {

        return notificationService
                .getNotificationsByStudentId(studentId);
    }


    // ==========================================
    // GET UNREAD NOTIFICATIONS
    // ==========================================
    @GetMapping("/notification/student/{studentId}/unread")
    public List<Notification> getUnreadNotifications(
            @PathVariable Long studentId) {

        return notificationService
                .getUnreadNotifications(studentId);
    }


    // ==========================================
    // COUNT UNREAD NOTIFICATIONS
    // ==========================================
    @GetMapping("/notification/student/{studentId}/unread/count")
    public long countUnreadNotifications(
            @PathVariable Long studentId) {

        return notificationService
                .countUnreadNotifications(studentId);
    }


    // ==========================================
    // MARK NOTIFICATION AS READ
    // ==========================================
    @PutMapping("/notification/{notificationId}/read")
    public Notification markAsRead(
            @PathVariable Long notificationId) {

        return notificationService
                .markAsRead(notificationId);
    }


    // ==========================================
    // MARK NOTIFICATION AS UNREAD
    // ==========================================
    @PutMapping("/notification/{notificationId}/unread")
    public Notification markAsUnread(
            @PathVariable Long notificationId) {

        return notificationService
                .markAsUnread(notificationId);
    }


    // ==========================================
    // DELETE ONE NOTIFICATION
    // ==========================================
    @DeleteMapping("/notification/{notificationId}")
    public String deleteNotification(
            @PathVariable Long notificationId) {

        notificationService
                .deleteNotification(notificationId);

        return "Notification deleted successfully";
    }


    // ==========================================
    // DELETE ALL STUDENT NOTIFICATIONS
    // ==========================================
    @DeleteMapping("/notification/student/{studentId}")
    public String deleteAllNotifications(
            @PathVariable Long studentId) {

        notificationService
                .deleteAllNotifications(studentId);

        return "All notifications deleted successfully";
    }
}