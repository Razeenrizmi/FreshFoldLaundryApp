package com.laundry.freshfoldlaundryapp.strategy;

import com.laundry.freshfoldlaundryapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidationStrategy implements UserValidationStrategy {

    private static final String PHONE_PATTERN = "^[0-9]{10}$";

    @Override
    public ValidationResult validate(User user) {
        String phoneNumber = user.getPhoneNumber();

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return new ValidationResult(false, "Phone number is required", "phoneNumber", "PHONE");
        }

        // First check if the original input contains only digits (no letters allowed)
        if (!phoneNumber.matches("^[0-9]+$")) {
            return new ValidationResult(false, "Phone number should contain only digits, no letters or special characters", "phoneNumber", "PHONE");
        }

        // Check length requirements
        if (phoneNumber.length() < 10) {
            return new ValidationResult(false, "Phone number must be at least 10 digits", "phoneNumber", "PHONE");
        }

        if (phoneNumber.length() > 10) {
            return new ValidationResult(false, "Phone number cannot exceed 10 digits", "phoneNumber", "PHONE");
        }

        // Final pattern check for exactly 10 digits
        if (!phoneNumber.matches(PHONE_PATTERN)) {
            return new ValidationResult(false, "Phone number must be exactly 10 digits", "phoneNumber", "PHONE");
        }

        return new ValidationResult(true, "Valid phone number", "phoneNumber", "PHONE");
    }

    @Override
    public String getValidationType() {
        return "PHONE";
    }
}
