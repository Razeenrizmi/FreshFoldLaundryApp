package com.laundry.freshfoldlaundryapp.repository.pickupDelivery;

import com.laundry.freshfoldlaundryapp.model.pickupDelivery.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository("pickupDeliveryOrderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByPickupDate(LocalDate pickupDate);
    List<Order> findByDeliveryDate(LocalDate deliveryDate);
    List<Order> findByStatusNot(String status);
    
    // Additional methods for driver assignments
    List<Order> findByPickupDriverId(Integer pickupDriverId);
    List<Order> findByDeliveryDriverId(Integer deliveryDriverId);
}