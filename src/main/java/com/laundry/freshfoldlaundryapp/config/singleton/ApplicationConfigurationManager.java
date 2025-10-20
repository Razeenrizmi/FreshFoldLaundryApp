package com.laundry.freshfoldlaundryapp.config.singleton;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApplicationConfigurationManager {

    private final Map<String, String> configurations;
    private final Map<String, String> runtimeConfigurations;

    @Value("${app.name:FreshFold Laundry App}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.max.file.upload.size:10MB}")
    private String maxFileUploadSize;

    public ApplicationConfigurationManager() {
        this.configurations = new ConcurrentHashMap<>();
        this.runtimeConfigurations = new ConcurrentHashMap<>();
    }

    @PostConstruct
    private void initializeConfigurations() {
        // Load default configurations
        configurations.put("app.name", appName);
        configurations.put("app.version", appVersion);
        configurations.put("app.max.file.upload.size", maxFileUploadSize);
        configurations.put("app.environment", "production");
        configurations.put("payment.retry.attempts", "3");
        configurations.put("order.auto.cancel.hours", "24");
        configurations.put("notification.email.enabled", "true");
        configurations.put("notification.sms.enabled", "true");

        // Laundry-specific configurations
        configurations.put("laundry.service.areas", "Downtown,Uptown,Suburbs");
        configurations.put("laundry.pickup.time.slots", "9-12,12-15,15-18,18-21");
        configurations.put("laundry.delivery.time.slots", "9-12,12-15,15-18,18-21");
        configurations.put("laundry.emergency.contact", "+1-800-LAUNDRY");
        configurations.put("laundry.price.per.kg.wash", "5.00");
        configurations.put("laundry.price.per.kg.dryclean", "12.00");

        System.out.println("✅ ApplicationConfigurationManager initialized with " + configurations.size() + " configurations");
    }

    public String getConfiguration(String key) {
        // Check runtime configurations first (higher priority)
        String value = runtimeConfigurations.get(key);
        if (value != null) {
            return value;
        }
        return configurations.get(key);
    }

    public void setRuntimeConfiguration(String key, String value) {
        runtimeConfigurations.put(key, value);
        System.out.println("🔧 Runtime configuration updated: " + key + " = " + value);
    }

    public void removeRuntimeConfiguration(String key) {
        runtimeConfigurations.remove(key);
    }

    public Map<String, String> getAllConfigurations() {
        Map<String, String> allConfigs = new HashMap<>(configurations);
        allConfigs.putAll(runtimeConfigurations); // Runtime configs override defaults
        return allConfigs;
    }

    public Map<String, String> getLaundryConfigurations() {
        Map<String, String> laundryConfigs = new HashMap<>();
        getAllConfigurations().entrySet().stream()
            .filter(entry -> entry.getKey().startsWith("laundry."))
            .forEach(entry -> laundryConfigs.put(entry.getKey(), entry.getValue()));
        return laundryConfigs;
    }

    public boolean isFeatureEnabled(String feature) {
        String value = getConfiguration(feature + ".enabled");
        return "true".equalsIgnoreCase(value);
    }

    public int getIntConfiguration(String key, int defaultValue) {
        try {
            String value = getConfiguration(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public double getDoubleConfiguration(String key, double defaultValue) {
        try {
            String value = getConfiguration(key);
            return value != null ? Double.parseDouble(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String[] getArrayConfiguration(String key) {
        String value = getConfiguration(key);
        return value != null ? value.split(",") : new String[0];
    }

    public void logCurrentConfigurations() {
        System.out.println("📋 Current Application Configurations:");
        getAllConfigurations().forEach((key, value) ->
            System.out.println("  " + key + " = " + value));
    }
}
