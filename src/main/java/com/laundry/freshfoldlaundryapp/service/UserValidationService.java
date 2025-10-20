package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.strategy.UserValidationStrategy;
import com.laundry.freshfoldlaundryapp.strategy.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserValidationService {

    private final List<UserValidationStrategy> validationStrategies;

    @Autowired
    public UserValidationService(List<UserValidationStrategy> validationStrategies) {
        this.validationStrategies = validationStrategies;
    }

    /**
     * Validates a user using all available validation strategies
     * @param user The user to validate
     * @return List of validation results from all strategies
     */
    public List<ValidationResult> validateUser(User user) {
        List<ValidationResult> results = new ArrayList<>();

        for (UserValidationStrategy strategy : validationStrategies) {
            ValidationResult result = strategy.validate(user);
            results.add(result);
        }

        return results;
    }

    /**
     * Validates a user and returns only the first error found
     * @param user The user to validate
     * @return The first validation error, or null if all validations pass
     */
    public ValidationResult validateUserFirstError(User user) {
        for (UserValidationStrategy strategy : validationStrategies) {
            ValidationResult result = strategy.validate(user);
            if (!result.isValid()) {
                return result;
            }
        }
        return new ValidationResult(true, "All validations passed");
    }

    /**
     * Validates a user using a specific validation strategy
     * @param user The user to validate
     * @param validationType The type of validation to perform
     * @return ValidationResult for the specific validation type
     */
    public ValidationResult validateUserByType(User user, String validationType) {
        for (UserValidationStrategy strategy : validationStrategies) {
            if (strategy.getValidationType().equals(validationType)) {
                return strategy.validate(user);
            }
        }
        return new ValidationResult(false, "Validation type not found: " + validationType);
    }

    /**
     * Checks if a user passes all validations
     * @param user The user to validate
     * @return true if all validations pass, false otherwise
     */
    public boolean isUserValid(User user) {
        return validationStrategies.stream()
                .allMatch(strategy -> strategy.validate(user).isValid());
    }
}
