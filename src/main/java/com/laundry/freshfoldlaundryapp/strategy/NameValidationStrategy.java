package com.laundry.freshfoldlaundryapp.strategy;

import com.laundry.freshfoldlaundryapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class NameValidationStrategy implements UserValidationStrategy {

    private static final String NAME_PATTERN = "^[a-zA-Z\\s]{2,30}$";

    @Override
    public ValidationResult validate(User user) {
        // Validate first name
        String firstName = user.getFirstName();
        if (firstName == null || firstName.trim().isEmpty()) {
            return new ValidationResult(false, "First name is required", "firstName", "NAME");
        }

        if (!firstName.matches(NAME_PATTERN)) {
            return new ValidationResult(false, "First name should contain only letters and be 2-30 characters long", "firstName", "NAME");
        }

        // Validate last name
        String lastName = user.getLastName();
        if (lastName == null || lastName.trim().isEmpty()) {
            return new ValidationResult(false, "Last name is required", "lastName", "NAME");
        }

        if (!lastName.matches(NAME_PATTERN)) {
            return new ValidationResult(false, "Last name should contain only letters and be 2-30 characters long", "lastName", "NAME");
        }

        return new ValidationResult(true, "Valid name", "name", "NAME");
    }

    @Override
    public String getValidationType() {
        return "NAME";
    }
}
