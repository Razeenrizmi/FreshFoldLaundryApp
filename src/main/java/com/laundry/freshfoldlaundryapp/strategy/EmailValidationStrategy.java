package com.laundry.freshfoldlaundryapp.strategy;

import com.laundry.freshfoldlaundryapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationStrategy implements UserValidationStrategy {

    private static final String EMAIL_PATTERN =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public ValidationResult validate(User user) {
        String email = user.getEmail();

        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Email is required", "email", "EMAIL");
        }

        if (!email.matches(EMAIL_PATTERN)) {
            return new ValidationResult(false, "Please enter a valid email address", "email", "EMAIL");
        }

        return new ValidationResult(true, "Valid email address", "email", "EMAIL");
    }

    @Override
    public String getValidationType() {
        return "EMAIL";
    }
}
