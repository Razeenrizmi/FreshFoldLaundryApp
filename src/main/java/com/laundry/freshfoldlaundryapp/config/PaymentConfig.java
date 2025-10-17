package com.laundry.freshfoldlaundryapp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    public static final String[] SUPPORTED_METHODS = {"Card", "CashOnDelivery"};
    public static final String DEFAULT_STATUS = "Pending";
}