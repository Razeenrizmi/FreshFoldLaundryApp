package com.laundry.freshfoldlaundryapp.service.payment.strategy;

public interface PaymentStrategy {
    PaymentResult processPayment(double amount, String paymentDetails, String customerEmail);
    String getPaymentMethodName();
    boolean isAvailable();
}
