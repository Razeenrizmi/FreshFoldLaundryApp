package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.dto.UserRegistrationDto;
import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean registerNewUser(UserRegistrationDto registrationDto) {
        try {
            // Check if user already exists
            Optional<User> existingUser = userRepository.findByEmail(registrationDto.getEmail());
            if (existingUser.isPresent()) {
                return false; // User already exists
            }

            User user = new User();
            user.setFirstName(registrationDto.getFirstName());
            user.setLastName(registrationDto.getLastName());
            user.setEmail(registrationDto.getEmail());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setPhoneNumber(registrationDto.getPhoneNumber());
            user.setAddress(registrationDto.getAddress()); // Set the address field
            user.setRole("CUSTOMER"); // Default role

            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null);
    }

    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }


    //Admin --->
    @Override
    public User saveUser(User user) {
        try {
            // If it's a new user and password is provided, encode it
            if (user.getId() == null && user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(user);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteUser(Long userId) {
        try {
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
