package com.carrental.userservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n=============== USER-SERVICE JWT FILTER ================");
        System.out.println("➡️ " + request.getMethod() + " " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        String email = null;
        String token = null;
        String role = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                email = jwtUtil.extractUsername(token);
                role = jwtUtil.extractRole(token);

                System.out.println("✅ Token valid");
                System.out.println("✅ Extracted email: " + email);
                System.out.println("✅ Extracted role: " + role);
            } else {
                System.out.println("❌ Invalid token");
            }
        } else {
            System.out.println("❌ No Bearer token found");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (role != null) {
                // ✅ FIX: Prevent ROLE_ROLE_ADMIN issue
                String finalRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                authorities.add(new SimpleGrantedAuthority(finalRole));
                System.out.println("✅ Final authority applied: " + finalRole);
            }

            User principal = new User(email, "", authorities);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            request.setAttribute("email", email);
            request.setAttribute("role", role);

            System.out.println("✅ SecurityContext SET with authorities: " + authorities);
        }

        System.out.println("=========================================================\n");

        filterChain.doFilter(request, response);
    }
}
