package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.service.UserValidationService;
import com.laundry.freshfoldlaundryapp.strategy.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/validation-demo")
public class ValidationDemoController {

    @Autowired
    private UserValidationService userValidationService;

    /**
     * Show the validation demo page
     */
    @GetMapping
    public String showValidationDemo(Model model) {
        model.addAttribute("user", new User());
        return "validation-demo";
    }

    /**
     * AJAX endpoint to validate user data in real-time
     */
    @PostMapping("/validate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validateUser(@RequestBody User user) {
        List<ValidationResult> results = userValidationService.validateUser(user);

        Map<String, Object> response = new HashMap<>();
        Map<String, ValidationResult> validationsByField = new HashMap<>();

        boolean allValid = true;
        for (ValidationResult result : results) {
            if (result.getFieldName() != null) {
                validationsByField.put(result.getFieldName(), result);
            }
            if (!result.isValid()) {
                allValid = false;
            }
        }

        response.put("validations", validationsByField);
        response.put("allValid", allValid);
        response.put("strategies", results);

        return ResponseEntity.ok(response);
    }

    /**
     * Validate specific field using strategy pattern
     */
    @PostMapping("/validate/{type}")
    @ResponseBody
    public ResponseEntity<ValidationResult> validateByType(@RequestBody User user, @PathVariable String type) {
        ValidationResult result = userValidationService.validateUserByType(user, type.toUpperCase());
        return ResponseEntity.ok(result);
    }

    /**
     * Get all available validation strategies
     */
    @GetMapping("/strategies")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getValidationStrategies() {
        Map<String, String> strategies = new HashMap<>();
        strategies.put("EMAIL", "Email Validation Strategy");
        strategies.put("PASSWORD", "Password Validation Strategy");
        strategies.put("NAME", "Name Validation Strategy");
        strategies.put("PHONE", "Phone Validation Strategy");

        return ResponseEntity.ok(strategies);
    }
}
