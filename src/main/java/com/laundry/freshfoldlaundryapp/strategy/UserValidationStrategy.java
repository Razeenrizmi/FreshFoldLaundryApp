package com.laundry.freshfoldlaundryapp.strategy;

import com.laundry.freshfoldlaundryapp.model.User;

public interface UserValidationStrategy {
    ValidationResult validate(User user);
    String getValidationType();
}
