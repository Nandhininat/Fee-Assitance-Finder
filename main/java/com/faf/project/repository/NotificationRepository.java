package com.faf.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faf.project.model.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByStudentId(
            Long studentId);


    List<Notification>
    findByStudentIdAndIsRead(
            Long studentId,
            boolean isRead);


    long countByStudentIdAndIsRead(
            Long studentId,
            boolean isRead);


    boolean existsByStudentIdAndTypeAndMessage(
            Long studentId,
            String type,
            String message);
}