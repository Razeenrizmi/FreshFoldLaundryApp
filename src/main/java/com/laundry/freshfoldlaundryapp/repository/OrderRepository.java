package com.laundry.freshfoldlaundryapp.repository;

import com.laundry.freshfoldlaundryapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders by customer ID
    List<Order> findByCustomerId(Long customerId);

    // Find orders by status
    List<Order> findByStatus(String status);

    // Find orders by payment method
    List<Order> findByPaymentMethod(String paymentMethod);

    // Find orders by payment status (needed for manager dashboard)
    List<Order> findByPaymentStatus(String paymentStatus);

    // Find orders by staff ID
    List<Order> findByStaffId(Long staffId);

    // Find orders by delivery staff ID
    List<Order> findByDeliveryStaffId(Long deliveryStaffId);

    // Find orders by customer and status
    List<Order> findByCustomerIdAndStatus(Long customerId, String status);

    // Find orders by customer and payment status
    List<Order> findByCustomerIdAndPaymentStatus(Long customerId, String paymentStatus);

    // Find orders by status ordered by order date descending
    List<Order> findByStatusOrderByOrderDateDesc(String status);

    // Find orders by payment status ordered by order date ascending
    List<Order> findByPaymentStatusOrderByOrderDateAsc(String paymentStatus);

    // Find all orders ordered by order date descending (for staff dashboard)
    List<Order> findAllByOrderByOrderDateDesc();
}
