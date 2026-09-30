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

import com.faf.project.model.Admin;
import com.faf.project.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;


    // ================= CREATE ADMIN =================

    @PostMapping("/admin")
    public Admin saveAdmin(
            @RequestBody Admin admin) {

        return adminService.saveAdmin(admin);
    }


    // ================= ADMIN LOGIN =================

    @PostMapping("/admin/login")
    public Admin login(
            @RequestBody Admin admin,
            HttpServletRequest request) {

        Admin loggedInAdmin =
                adminService.login(
                        admin.getUsername(),
                        admin.getPassword()
                );

        HttpSession session =
                request.getSession(true);

        session.setAttribute(
                "adminId",
                loggedInAdmin.getAdminID()
        );

        session.setAttribute(
                "adminRole",
                "ADMIN"
        );

        return loggedInAdmin;
    }


    // ================= ADMIN LOGOUT =================

    @PostMapping("/admin/logout")
    public String logout(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "Admin logged out successfully";
    }


    // ================= GET ALL ADMINS =================

    @GetMapping("/admin")
    public List<Admin> getAllAdmins() {

        return adminService.getAllAdmins();
    }


    // ================= GET ADMIN BY ID =================

    @GetMapping("/admin/{adminId:\\d+}")
    public Admin getAdminById(
            @PathVariable Long adminId) {

        return adminService.getAdminById(adminId);
    }


    // ================= UPDATE ADMIN =================

    @PutMapping("/admin/{adminId}")
    public Admin updateAdmin(
            @PathVariable Long adminId,
            @RequestBody Admin admin) {

        return adminService.updateAdmin(
                adminId,
                admin
        );
    }


    // ================= DELETE ADMIN =================

    @DeleteMapping("/admin/{adminId}")
    public String deleteAdmin(
            @PathVariable Long adminId) {

        adminService.deleteAdmin(adminId);

        return "Admin deleted successfully";
    }
}