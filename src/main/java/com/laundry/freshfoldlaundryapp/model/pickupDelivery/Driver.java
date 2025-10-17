package com.laundry.freshfoldlaundryapp.model.pickupDelivery;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contact;

    // Remove the @OneToMany mappings since Orders model is not a JPA entity
    // and doesn't have pickupDriver/deliveryDriver fields
    // Instead, we'll handle these relationships through service methods
    
    // Note: If you need to get orders for a driver, use the OrderService.getOrdersForDriver(driverId) method

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}