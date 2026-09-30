package com.faf.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.faf.project.model.Admin;
import com.faf.project.repository.AdminRepository;

@Service
public class AdminUserDetailsService
        implements UserDetailsService {

    @Autowired
    AdminRepository adminRepository;


    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        Admin admin =
                adminRepository.findByUsername(username);

        if (admin == null) {
            throw new UsernameNotFoundException(
                    "Admin not found"
            );
        }

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles(admin.getRole().name().toUpperCase())
                .build();
    }
}