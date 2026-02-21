package com.carrental.userservice.service.impl;

import com.carrental.userservice.client.PaymentClient;
import com.carrental.userservice.dto.CreateUserRequest;
import com.carrental.userservice.dto.UpdateUserRequest;
import com.carrental.userservice.entity.User;
import com.carrental.userservice.entity.UserRole;
import com.carrental.userservice.repository.UserRepository;
import com.carrental.userservice.service.CloudinaryUploadService;
import com.carrental.userservice.service.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentClient paymentClient;
    private final CloudinaryUploadService cloudinaryUploadService;
    private final Cloudinary cloudinary;




//    @Override
//    public User registerUser(CreateUserRequest request) {
//
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            throw new RuntimeException("Email already registered: " + request.getEmail());
//        }
//
//        UserRole role = UserRole.fromString(request.getRole());
//        if (role == null) role = UserRole.CUSTOMER;
//
//        // ✅ Encode password properly
//        String encodedPassword = passwordEncoder.encode(request.getPassword());
//
//        User user = User.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .phone(request.getPhone())
//                .password(encodedPassword)
//                .address(request.getAddress())
//                .role(role)
//                .enabled(true)
//                .isVerified(false)
//                .build();
//
//        User savedUser = userRepository.save(user);
//
//        // ✅ Wallet creation for OWNER (non-blocking)
//        if (savedUser.getRole() == UserRole.OWNER) {
//            try {
//                paymentClient.createWallet(savedUser.getEmail(), savedUser.getId());
//            } catch (Exception e) {
//                log.error("Wallet creation failed for owner {}", savedUser.getEmail(), e);
//            }
//        }
//
//        return savedUser;
//    }


    @Override
    public User registerUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        UserRole role = request.getRole() == null
                ? UserRole.CUSTOMER
                : UserRole.fromString(request.getRole());

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(encodedPassword)
                .address(request.getAddress())
                .role(role)
                .enabled(true)
                .isVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == UserRole.OWNER) {
            try {
                paymentClient.createWallet(savedUser.getEmail(), savedUser.getId());
            } catch (Exception e) {
                log.error("Wallet creation failed for owner {}", savedUser.getEmail(), e);
            }
        }

        return savedUser;
    }

    @Override
    public void updateUserIdentity(String email,
                                   MultipartFile license,
                                   MultipartFile aadhar) {

        try {

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Upload License
            Map licenseUpload = cloudinary.uploader().upload(
                    license.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "identity_documents",
                            "resource_type", "auto"
                    )
            );

            // Upload Aadhar
            Map aadharUpload = cloudinary.uploader().upload(
                    aadhar.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "identity_documents",
                            "resource_type", "auto"
                    )
            );

            String licenseUrl = licenseUpload.get("secure_url").toString();
            String aadharUrl = aadharUpload.get("secure_url").toString();

            user.setLicenseUrl(licenseUrl);
            user.setAadharUrl(aadharUrl);
            user.setVerified(true);

            userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
        }
    }



    @Override
    public User updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, UserRole newRole) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if ("scindia@gmail.com".equals(user.getEmail())) {
            throw new RuntimeException("Cannot modify primary Super Admin role.");
        }

        user.setRole(newRole);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void toggleUserAccountStatus(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if ("scindia@gmail.com".equals(user.getEmail())) {
            throw new RuntimeException("Cannot deactivate primary Super Admin.");
        }

        boolean currentStatus = user.getEnabled() != null ? user.getEnabled() : true;
        user.setEnabled(!currentStatus);

        userRepository.save(user);
    }
}
