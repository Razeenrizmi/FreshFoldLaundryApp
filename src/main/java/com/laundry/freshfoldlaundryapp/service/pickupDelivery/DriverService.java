package com.laundry.freshfoldlaundryapp.service.pickupDelivery;

import com.laundry.freshfoldlaundryapp.model.pickupDelivery.Driver;
import com.laundry.freshfoldlaundryapp.repository.pickupDelivery.DriverRepository;
import com.laundry.freshfoldlaundryapp.repository.pickupDelivery.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    @Qualifier("pickupDeliveryOrderRepository")
    private OrderRepository orderRepository;

    // Saves a new driver
    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    // Gets all drivers
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    // Gets a driver by ID
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id).orElse(null);
    }

    // Add test drivers on startup (for beginner: this runs once when app starts, adds 2 drivers if DB empty)
    @PostConstruct
    public void initDrivers() {
        if (getAllDrivers().isEmpty()) {
            Driver driver1 = new Driver();
            driver1.setName("Driver1");
            driver1.setContact("123456");
            saveDriver(driver1);

            Driver driver2 = new Driver();
            driver2.setName("Driver2");
            driver2.setContact("789012");
            saveDriver(driver2);
        }
    }

    // Gets available drivers for delivery (not currently assigned to active delivery tasks)
    public List<Driver> getAvailableDeliveryDrivers() {
        // TEMPORARY: Return all drivers for testing
        List<Driver> allDrivers = getAllDrivers();
        System.out.println("DEBUG DriverService: Returning all " + allDrivers.size() + " drivers for testing");
        return allDrivers;

        /* Original filtering logic - temporarily commented out
        List<Driver> allDrivers = getAllDrivers();

        // Get orders that are in progress for delivery (not yet delivered)
        List<String> activeStatuses = List.of("READY_FOR_DELIVERY", "OUT_FOR_DELIVERY");

        return allDrivers.stream()
                .filter(driver -> {
                    // Check if driver is currently assigned to any active delivery orders
                    return activeStatuses.stream().noneMatch(status ->
                            orderRepository.findByStatus(status).stream()
                                    .anyMatch(order ->
                                            order.getDeliveryDriverId() != null &&
                                                    order.getDeliveryDriverId().equals(driver.getId().intValue())
                                    )
                    );
                })
                .collect(Collectors.toList());
        */
    }
}