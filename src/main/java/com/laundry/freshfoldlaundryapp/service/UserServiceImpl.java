package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.dto.UserRegistrationDto;
import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.repository.UserRepository;
import com.laundry.freshfoldlaundryapp.strategy.ValidationResult;
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

    @Autowired
    private UserValidationService userValidationService;

    @Override
    public boolean registerNewUser(UserRegistrationDto registrationDto) {
        try {
            //  Strategy Pattern Implementation - Use validation strategies
            System.out.println("Starting user registration with Strategy Pattern validation...");

            // Convert DTO to User object for validation
            User userToValidate = new User();
            userToValidate.setFirstName(registrationDto.getFirstName());
            userToValidate.setLastName(registrationDto.getLastName());
            userToValidate.setEmail(registrationDto.getEmail());
            userToValidate.setPassword(registrationDto.getPassword()); // Don't encode yet for validation
            userToValidate.setPhoneNumber(registrationDto.getPhoneNumber());
            userToValidate.setAddress(registrationDto.getAddress());

            List<ValidationResult> validationResults = userValidationService.validateUser(userToValidate);

            // Check if any validation failed
            for (ValidationResult result : validationResults) {
                if (!result.isValid()) {
                    System.out.println("❌ Registration failed - " + result.getValidationType() +
                                     ": " + result.getErrorMessage());
                    return false;
                }
            }

            System.out.println("✅ All Strategy Pattern validations passed!");

            // Check if user already exists
            Optional<User> existingUser = userRepository.findByEmail(registrationDto.getEmail());
            if (existingUser.isPresent()) {
                System.out.println("❌ Registration failed - User already exists with email: " + registrationDto.getEmail());
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
            System.out.println(" User registration successful for: " + registrationDto.getEmail());
            return true;
        } catch (Exception e) {
            System.out.println(" Registration failed with exception: " + e.getMessage());
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
