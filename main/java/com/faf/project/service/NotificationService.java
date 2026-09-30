package com.faf.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faf.project.exception.ResourceNotFoundException;
import com.faf.project.model.AssistanceScheme;
import com.faf.project.model.Notification;
import com.faf.project.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    // ==========================================
    // SAVE NOTIFICATION
    // ==========================================
    public Notification saveNotification(Notification notification) {

        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }

        return notificationRepository.save(notification);
    }


    // ==========================================
    // GET ALL NOTIFICATIONS
    // ==========================================
    public List<Notification> getAllNotifications() {

        return notificationRepository.findAll();
    }


    // ==========================================
    // GET NOTIFICATION BY ID
    // ==========================================
    public Notification getNotificationById(Long notificationId) {

        return notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification not found with ID: "
                                        + notificationId
                        ));
    }


    // ==========================================
    // GET NOTIFICATIONS BY STUDENT
    // ==========================================
    public List<Notification> getNotificationsByStudentId(Long studentId) {

        return notificationRepository.findByStudentId(studentId);
    }


    // ==========================================
    // GET UNREAD NOTIFICATIONS
    // ==========================================
    public List<Notification> getUnreadNotifications(Long studentId) {

        return notificationRepository
                .findByStudentIdAndIsRead(studentId, false);
    }


    // ==========================================
    // COUNT UNREAD NOTIFICATIONS
    // ==========================================
    public long countUnreadNotifications(Long studentId) {

        return notificationRepository
                .countByStudentIdAndIsRead(studentId, false);
    }


    // ==========================================
    // MARK AS READ
    // ==========================================
    public Notification markAsRead(Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with ID: "
                                                + notificationId
                                ));

        // Change read status
        notification.setRead(true);

        // Save permanently into database
        return notificationRepository.save(notification);
    }


    // ==========================================
    // MARK AS UNREAD
    // ==========================================
    public Notification markAsUnread(Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with ID: "
                                                + notificationId
                                ));

        // Change read status
        notification.setRead(false);

        // Save permanently into database
        return notificationRepository.save(notification);
    }


    // ==========================================
    // DELETE NOTIFICATION
    // ==========================================
    public void deleteNotification(Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with ID: "
                                                + notificationId
                                ));

        notificationRepository.delete(notification);
    }


    // ==========================================
    // DELETE ALL NOTIFICATIONS OF A STUDENT
    // ==========================================
    public void deleteAllNotifications(Long studentId) {

        List<Notification> notifications =
                notificationRepository.findByStudentId(studentId);

        notificationRepository.deleteAll(notifications);
    }


    // ==========================================
    // ELIGIBILITY NOTIFICATION
    // ==========================================
    public Notification createEligibilityNotification(
            Long studentId,
            AssistanceScheme scheme) {

        String message =
                "You are eligible for "
                + scheme.getSchemeName()
                + ". Please check the scholarship details and apply before the deadline.";


        // Prevent duplicate notification
        boolean alreadyExists =
                notificationRepository
                        .existsByStudentIdAndTypeAndMessage(
                                studentId,
                                "ELIGIBILITY",
                                message
                        );

        if (alreadyExists) {
            return null;
        }


        Notification notification = new Notification();

        notification.setStudentId(studentId);

        notification.setTitle(
                "Scholarship Eligibility"
        );

        notification.setMessage(message);

        notification.setType(
                "ELIGIBILITY"
        );

        notification.setRead(false);

        notification.setCreatedAt(
                LocalDateTime.now()
        );


        return notificationRepository.save(notification);
    }


    // ==========================================
    // DEADLINE NOTIFICATION
    // ==========================================
    public Notification createDeadlineNotification(
            Long studentId,
            AssistanceScheme scheme) {

        if (scheme.getApplicationDeadline() == null) {
            return null;
        }

        String message =
                "The application deadline for "
                + scheme.getSchemeName()
                + " is "
                + scheme.getApplicationDeadline()
                + ". Please apply before the deadline.";


        // Prevent duplicate notification
        boolean alreadyExists =
                notificationRepository
                        .existsByStudentIdAndTypeAndMessage(
                                studentId,
                                "DEADLINE",
                                message
                        );

        if (alreadyExists) {
            return null;
        }


        Notification notification = new Notification();

        notification.setStudentId(studentId);

        notification.setTitle(
                "Scholarship Deadline Reminder"
        );

        notification.setMessage(message);

        notification.setType(
                "DEADLINE"
        );

        notification.setRead(false);

        notification.setCreatedAt(
                LocalDateTime.now()
        );


        return notificationRepository.save(notification);
    }


    // ==========================================
    // CHECK DEADLINE AND SEND REMINDER
    // ==========================================
    public void checkDeadlineAndNotify(
            Long studentId,
            AssistanceScheme scheme) {

        // No deadline
        if (scheme.getApplicationDeadline() == null) {
            return;
        }


        LocalDate today = LocalDate.now();

        LocalDate deadline =
                scheme.getApplicationDeadline();

        // Reminder starts 7 days before deadline
        LocalDate reminderDate =
                deadline.minusDays(7);


        /*
         * Notification will be created when:
         *
         * today >= reminder date
         * AND
         * today <= deadline
         */
        if (!today.isBefore(reminderDate)
                && !today.isAfter(deadline)) {

            createDeadlineNotification(
                    studentId,
                    scheme
            );
        }
    }
}