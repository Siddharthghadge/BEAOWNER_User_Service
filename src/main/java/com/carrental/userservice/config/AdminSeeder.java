package com.carrental.userservice.config;

import com.carrental.userservice.entity.User;
import com.carrental.userservice.entity.UserRole;
import com.carrental.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("scindia@gmail.com").isEmpty()) {
            User admin = User.builder()
                    .name("System Admin")
                    .email("scindia@gmail.com")
                    .password(passwordEncoder.encode("Sid@5131"))
                    .role(UserRole.ADMIN) // Using your Enum
                    .enabled(true)
                    .isVerified(true) // Admin is pre-verified
                    .build();

            userRepository.save(admin);
            System.out.println("✅ ADMIN SEEDER: Super Admin created successfully.");
        }
    }
}