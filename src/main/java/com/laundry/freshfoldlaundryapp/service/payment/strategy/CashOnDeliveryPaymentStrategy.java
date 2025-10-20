package com.laundry.freshfoldlaundryapp.service.payment.strategy;

import org.springframework.stereotype.Component;

@Component
public class CashOnDeliveryPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(double amount, String paymentDetails, String customerEmail) {
        try {
            System.out.println("Processing Cash on Delivery for $" + amount + " for " + customerEmail);

            if (amount <= 0) {
                return new PaymentResult(false, "Invalid amount", "INVALID_AMOUNT");
            }

            // COD doesn't require payment processing, just record the order
            String transactionId = "COD_" + System.currentTimeMillis();
            PaymentResult result = new PaymentResult(true, "Cash on Delivery order recorded successfully",
                                                   transactionId, "CASH_ON_DELIVERY");
            result.setAmount(amount);

            return result;

        } catch (Exception e) {
            return new PaymentResult(false, "COD processing failed: " + e.getMessage(), "COD_ERROR");
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash on Delivery";
    }

    @Override
    public boolean isAvailable() {
        return true; // COD is always available for laundry services
    }
}
