package com.faf.project.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {

            Object adminId =
                    session.getAttribute("adminId");

            Object adminRole =
                    session.getAttribute("adminRole");

            if (adminId != null
                    && "ADMIN".equals(adminRole)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                "admin-" + adminId,
                                null,
                                AuthorityUtils.createAuthorityList(
                                        "ROLE_ADMIN"
                                )
                        );

                SecurityContext context =
                        SecurityContextHolder.createEmptyContext();

                context.setAuthentication(authentication);

                SecurityContextHolder.setContext(context);

                session.setAttribute(
                        HttpSessionSecurityContextRepository
                                .SPRING_SECURITY_CONTEXT_KEY,
                        context
                );
            }
        }

        try {

            filterChain.doFilter(request, response);

        } finally {

            SecurityContextHolder.clearContext();
        }
    }
}