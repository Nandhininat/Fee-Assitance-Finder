package com.faf.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.faf.project.exception.ResourceNotFoundException;
import com.faf.project.model.Admin;
import com.faf.project.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // ================= SAVE ADMIN =================

    public Admin saveAdmin(Admin admin) {

        Admin existing =
                adminRepository.findByUsername(
                        admin.getUsername()
                );


        if (existing != null) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }


        if (admin.getPassword() == null
                || admin.getPassword().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password is required"
            );
        }


        admin.setPassword(
                passwordEncoder.encode(
                        admin.getPassword()
                )
        );


        return adminRepository.save(admin);
    }


    // ================= GET ALL =================

    public List<Admin> getAllAdmins() {

        return adminRepository.findAll();
    }


    // ================= GET BY ID =================

    public Admin getAdminById(
            Long adminId) {

        return adminRepository.findById(adminId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        "Admin not found with ID: "
                        + adminId
                    )
                );
    }


    // ================= LOGIN =================

    public Admin login(
            String username,
            String password) {

        Admin admin =
                adminRepository.findByUsername(
                        username
                );


        if (admin == null) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }


        if (admin.getPassword() == null
                || !passwordEncoder.matches(
                        password,
                        admin.getPassword()
                )) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }


        return admin;
    }


    // ================= UPDATE =================

    public Admin updateAdmin(
            Long adminId,
            Admin admin) {

        Admin existingAdmin =
                adminRepository.findById(adminId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        "Admin not found with ID: "
                        + adminId
                    )
                );


        existingAdmin.setUsername(
                admin.getUsername()
        );


        if (admin.getPassword() != null
                && !admin.getPassword().isBlank()) {

            existingAdmin.setPassword(
                    passwordEncoder.encode(
                            admin.getPassword()
                    )
            );
        }


        if (admin.getRole() != null) {

            existingAdmin.setRole(
                    admin.getRole()
            );
        }


        return adminRepository.save(
                existingAdmin
        );
    }


    // ================= DELETE =================

    public void deleteAdmin(
            Long adminId) {

        Admin admin =
                adminRepository.findById(adminId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        "Admin not found with ID: "
                        + adminId
                    )
                );


        adminRepository.delete(admin);
    }
}