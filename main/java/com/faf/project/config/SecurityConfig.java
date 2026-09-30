package com.faf.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AdminAuthenticationFilter adminAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())

            // Session based admin authentication
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.IF_REQUIRED
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Frontend pages
                .requestMatchers(
                    "/",
                    "/login.html",
                    "/login.css",
                    "/login.js",

                    "/dashboard.html",
                    "/dashboard.css",
                    "/dashboard.js",

                    "/schemes.html",
                    "/schemes.css",
                    "/schemes.js",

                    "/scheme-details.html",
                    "/scheme-details.css",
                    "/scheme-details.js",

                    "/bookmarks.html",
                    "/bookmarks.css",
                    "/bookmarks.js",

                    "/notification.html",
                    "/notification.css",
                    "/notification.js",

                    "/profile.html",
                    "/profile.css",
                    "/profile.js",

                    "/admin.html",
                    "/admin.css",
                    "/admin.js"
                ).permitAll()

                // Student login
                .requestMatchers("/login").permitAll()

                // Admin login
                .requestMatchers("/admin/login").permitAll()

                // Admin APIs
                .requestMatchers(HttpMethod.POST, "/admin").permitAll()
                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")

                // Other APIs
                .anyRequest().permitAll()
            )

            // Disable default login page
            .formLogin(form -> form.disable())

            // Disable HTTP Basic
            .httpBasic(basic -> basic.disable());

        // Add custom admin authentication filter
        http.addFilterBefore(
            adminAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}