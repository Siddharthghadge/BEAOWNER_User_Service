package com.carrental.userservice.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");

        if (cloudinaryUrl == null || cloudinaryUrl.isBlank()) {
            throw new RuntimeException("CLOUDINARY_URL environment variable not set!");
        }

        return new Cloudinary(cloudinaryUrl);
    }
}

