package com.laundry.freshfoldlaundryapp.service.payment;

import com.laundry.freshfoldlaundryapp.dto.payment.PaymentDto;
import com.laundry.freshfoldlaundryapp.model.payment.Payment;
import com.laundry.freshfoldlaundryapp.repository.payment.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void processPayment(PaymentDto dto) {
        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setAmount(dto.getAmount().doubleValue()); // Convert BigDecimal to Double
        payment.setPaymentStatus("Pending");
        payment.setPaymentMethod(dto.getPaymentMethod().toString()); // Convert PaymentMethod to String
        payment.setPaymentDatetime(dto.getPaymentDatetime());

        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public void markAsCOD(int orderId) {
        paymentRepository.updateStatus(orderId, "Pending", "CashOnDelivery");
    }

    @Override
    public void markAsPaid(int orderId) {
        paymentRepository.updateStatus(orderId, "Paid", "CARD");
    }

    @Override
    public Payment getPaymentByOrderId(int orderId) {
        return paymentRepository.findByOrderId(orderId).orElse(null);
    }

    @Override
    public void markAsCancelled(int orderId) {
        paymentRepository.updateStatus(orderId, "Cancelled", "CashOnDelivery");
    }

    // ✅ Fixed: unwrap Optional safely
    // Fixed: use String and Double instead of PaymentMethod enum and BigDecimal
    public void recordPayment(int orderId, BigDecimal amount, String method, String status) {
        Optional<Payment> optionalPayment = paymentRepository.findLatestByOrderId(orderId);

        if (optionalPayment.isPresent()) {
            Payment existing = optionalPayment.get();
            existing.setPaymentMethod(method); // Fixed: pass the method parameter
            existing.setPaymentStatus(status);
            existing.setAmount(amount.doubleValue()); // Convert BigDecimal to Double
            existing.setPaymentDatetime(LocalDateTime.now());
            paymentRepository.updateStatus(existing.getOrderId(), status, method);
        } else {
            Payment payment = new Payment(); // Fixed: declare payment variable
            payment.setOrderId(orderId);
            payment.setPaymentMethod(method); // Now String instead of enum
            payment.setPaymentStatus(status);
            payment.setAmount(amount.doubleValue()); // Convert BigDecimal to Double
            payment.setPaymentDatetime(LocalDateTime.now());
            paymentRepository.save(payment);
        }
    }
}
