package com.laundry.freshfoldlaundryapp.dto.payment;

import com.laundry.freshfoldlaundryapp.model.payment.PaymentMethod;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    private PaymentMethod paymentMethod;

    @NotBlank(message = "Payment status is required")
    private String paymentStatus;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Payment date and time is required")
    private LocalDateTime paymentDatetime;

    public PaymentDto() {
    }

    public PaymentDto(Integer orderId, PaymentMethod paymentMethod, String paymentStatus, BigDecimal amount, LocalDateTime paymentDatetime) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.paymentDatetime = paymentDatetime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public @NotBlank(message = "Payment method is required") PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(@NotBlank(message = "Payment method is required") PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDatetime() {
        return paymentDatetime;
    }

    public void setPaymentDatetime(LocalDateTime paymentDatetime) {
        this.paymentDatetime = paymentDatetime;
    }
}

