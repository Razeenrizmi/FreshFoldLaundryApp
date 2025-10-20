package com.laundry.freshfoldlaundryapp.service.payment.strategy;

import org.springframework.stereotype.Component;

@Component
public class PayPalPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(double amount, String paymentDetails, String customerEmail) {
        try {
            System.out.println("Processing PayPal payment of $" + amount + " for " + customerEmail);

            if (amount <= 0) {
                return new PaymentResult(false, "Invalid amount", "INVALID_AMOUNT");
            }

            if (customerEmail == null || !customerEmail.contains("@")) {
                return new PaymentResult(false, "Invalid email for PayPal", "INVALID_EMAIL");
            }

            // Simulate PayPal API call
            Thread.sleep(800);

            String transactionId = "PP_" + System.currentTimeMillis();
            PaymentResult result = new PaymentResult(true, "PayPal payment processed successfully",
                                                   transactionId, "PAYPAL");
            result.setAmount(amount);

            return result;

        } catch (Exception e) {
            return new PaymentResult(false, "PayPal processing failed: " + e.getMessage(), "PAYPAL_ERROR");
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "PayPal";
    }

    @Override
    public boolean isAvailable() {
        return true; // PayPal is available
    }
}
