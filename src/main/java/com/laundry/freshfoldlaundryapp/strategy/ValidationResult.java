package com.laundry.freshfoldlaundryapp.strategy;

public class ValidationResult {
    private boolean valid;
    private String message;
    private String fieldName;
    private String validationType; // Add validation type field

    public ValidationResult(boolean valid, String message, String fieldName) {
        this.valid = valid;
        this.message = message;
        this.fieldName = fieldName;
    }

    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    // Add constructor with validation type
    public ValidationResult(boolean valid, String message, String fieldName, String validationType) {
        this.valid = valid;
        this.message = message;
        this.fieldName = fieldName;
        this.validationType = validationType;
    }

    // Getters and setters
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    // Add getErrorMessage method for backward compatibility
    public String getErrorMessage() {
        return this.message;
    }
}
