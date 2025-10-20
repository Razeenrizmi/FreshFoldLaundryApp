package com.laundry.freshfoldlaundryapp.strategy;

import com.laundry.freshfoldlaundryapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidationStrategy implements UserValidationStrategy {

    @Override
    public ValidationResult validate(User user) {
        String password = user.getPassword();

        if (password == null || password.trim().isEmpty()) {
            return new ValidationResult(false, "Password is required", "password", "PASSWORD");
        }

        if (password.length() < 8) {
            return new ValidationResult(false, "Password must be at least 8 characters long", "password", "PASSWORD");
        }

        if (!password.matches(".*[A-Z].*")) {
            return new ValidationResult(false, "Password must contain at least one uppercase letter", "password", "PASSWORD");
        }

        if (!password.matches(".*[a-z].*")) {
            return new ValidationResult(false, "Password must contain at least one lowercase letter", "password", "PASSWORD");
        }

        if (!password.matches(".*\\d.*")) {
            return new ValidationResult(false, "Password must contain at least one number", "password", "PASSWORD");
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return new ValidationResult(false, "Password must contain at least one special character", "password", "PASSWORD");
        }

        return new ValidationResult(true, "Strong password", "password", "PASSWORD");
    }

    @Override
    public String getValidationType() {
        return "PASSWORD";
    }
}
