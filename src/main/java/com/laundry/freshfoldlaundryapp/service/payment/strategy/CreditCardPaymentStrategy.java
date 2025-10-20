package com.laundry.freshfoldlaundryapp.service.payment.strategy;

import org.springframework.stereotype.Component;

@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(double amount, String paymentDetails, String customerEmail) {
        try {
            // Simulate credit card processing
            System.out.println("Processing Credit Card payment of $" + amount + " for " + customerEmail);

            // Basic validation
            if (amount <= 0) {
                return new PaymentResult(false, "Invalid amount", "INVALID_AMOUNT");
            }

            if (paymentDetails == null || paymentDetails.length() < 16) {
                return new PaymentResult(false, "Invalid card details", "INVALID_CARD");
            }

            // Simulate processing delay
            Thread.sleep(1000);

            String transactionId = "CC_" + System.currentTimeMillis();
            PaymentResult result = new PaymentResult(true, "Credit card payment processed successfully",
                                                   transactionId, "CREDIT_CARD");
            result.setAmount(amount);

            return result;

        } catch (Exception e) {
            return new PaymentResult(false, "Credit card processing failed: " + e.getMessage(), "PROCESSING_ERROR");
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "Credit Card";
    }

    @Override
    public boolean isAvailable() {
        return true; // Credit card is always available
    }
}
