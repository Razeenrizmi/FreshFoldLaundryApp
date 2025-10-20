package com.laundry.freshfoldlaundryapp.service.payment.strategy;

import java.time.LocalDateTime;

public class PaymentResult {
    private boolean success;
    private String message;
    private String transactionId;
    private String paymentMethod;
    private double amount;
    private LocalDateTime processedAt;
    private String errorCode;

    public PaymentResult(boolean success, String message, String transactionId, String paymentMethod) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
        this.paymentMethod = paymentMethod;
        this.processedAt = LocalDateTime.now();
    }

    public PaymentResult(boolean success, String message, String errorCode) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.processedAt = LocalDateTime.now();
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
}
