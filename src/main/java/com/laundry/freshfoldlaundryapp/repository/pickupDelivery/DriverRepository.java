package com.laundry.freshfoldlaundryapp.repository.pickupDelivery;

import com.laundry.freshfoldlaundryapp.model.pickupDelivery.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}