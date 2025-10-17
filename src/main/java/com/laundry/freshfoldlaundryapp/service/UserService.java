package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.dto.UserRegistrationDto;
import com.laundry.freshfoldlaundryapp.model.User;
import java.util.List;

public interface UserService {
    boolean registerNewUser(UserRegistrationDto registrationDto);
    User findByEmail(String email);
    User findById(Long id);
    void save(User user);
    List<User> getUsersByRole(String role);
    User getUserById(Long userId);
    User saveUser(User user);
    boolean deleteUser(Long userId);
}