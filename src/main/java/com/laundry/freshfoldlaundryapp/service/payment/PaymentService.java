package com.laundry.freshfoldlaundryapp.service.payment;

import com.laundry.freshfoldlaundryapp.dto.payment.PaymentDto;
import com.laundry.freshfoldlaundryapp.model.payment.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    void processPayment(PaymentDto dto);
    List<Payment> getAllPayments();
    void markAsCOD(int orderId);
    void markAsPaid(int orderId);
    Payment getPaymentByOrderId(int orderId);
    void markAsCancelled(int orderId);

    void recordPayment(int orderId, BigDecimal amount, String card, String paid);
}



