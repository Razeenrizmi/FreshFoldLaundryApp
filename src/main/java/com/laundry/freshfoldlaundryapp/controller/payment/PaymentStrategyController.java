package com.laundry.freshfoldlaundryapp.controller.payment;

import com.laundry.freshfoldlaundryapp.service.payment.strategy.PaymentStrategyService;
import com.laundry.freshfoldlaundryapp.service.payment.strategy.PaymentResult;
import com.laundry.freshfoldlaundryapp.config.singleton.ApplicationConfigurationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentStrategyController {

    private final PaymentStrategyService paymentStrategyService;
    private final ApplicationConfigurationManager configManager;

    @Autowired
    public PaymentStrategyController(PaymentStrategyService paymentStrategyService,
                                   ApplicationConfigurationManager configManager) {
        this.paymentStrategyService = paymentStrategyService;
        this.configManager = configManager;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest request) {
        // Using Strategy Pattern to process payment
        PaymentResult result = paymentStrategyService.processPayment(
            request.getPaymentMethod(),
            request.getAmount(),
            request.getPaymentDetails(),
            request.getCustomerEmail()
        );

        // Using Singleton Pattern to log with app configuration
        String appVersion = configManager.getConfiguration("app.version");
        System.out.println("Payment processed in " + configManager.getConfiguration("app.name") + " v" + appVersion);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/methods")
    public ResponseEntity<Map<String, String>> getAvailablePaymentMethods() {
        Map<String, String> methods = paymentStrategyService.getAvailablePaymentMethods();
        return ResponseEntity.ok(methods);
    }

    @GetMapping("/config/laundry")
    public ResponseEntity<Map<String, String>> getLaundryConfigurations() {
        // Using Singleton Pattern to get laundry-specific configurations
        Map<String, String> laundryConfigs = configManager.getLaundryConfigurations();
        return ResponseEntity.ok(laundryConfigs);
    }

    @PostMapping("/config/runtime")
    public ResponseEntity<String> updateRuntimeConfiguration(@RequestParam String key, @RequestParam String value) {
        // Using Singleton Pattern to update runtime configuration
        configManager.setRuntimeConfiguration(key, value);
        return ResponseEntity.ok("Configuration updated successfully");
    }

    // Inner class for request body
    public static class PaymentRequest {
        private String paymentMethod;
        private double amount;
        private String paymentDetails;
        private String customerEmail;

        // Constructors
        public PaymentRequest() {}

        // Getters and setters
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public String getPaymentDetails() { return paymentDetails; }
        public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }

        public String getCustomerEmail() { return customerEmail; }
        public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    }
}
