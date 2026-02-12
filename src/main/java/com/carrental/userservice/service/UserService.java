package com.carrental.userservice.service;

import com.carrental.userservice.dto.*;
import com.carrental.userservice.entity.User;
import com.carrental.userservice.entity.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    User registerUser(CreateUserRequest request);

    User updateUser(Long id, UpdateUserRequest request);

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    void deleteUser(Long id);

    void updateUserIdentity(String email,
                            MultipartFile license,
                            MultipartFile aadhar) throws IOException;


    void updateUserRole(Long userId, UserRole newRole);

    void toggleUserAccountStatus(Long userId);
}
