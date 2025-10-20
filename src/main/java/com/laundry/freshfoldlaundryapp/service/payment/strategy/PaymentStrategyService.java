package com.laundry.freshfoldlaundryapp.service.payment.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentStrategyService {

    private final Map<String, PaymentStrategy> strategies;

    @Autowired
    public PaymentStrategyService(List<PaymentStrategy> strategyList) {
        strategies = new HashMap<>();

        // Register all payment strategies
        for (PaymentStrategy strategy : strategyList) {
            if (strategy instanceof CreditCardPaymentStrategy) {
                strategies.put("CREDIT_CARD", strategy);
            } else if (strategy instanceof PayPalPaymentStrategy) {
                strategies.put("PAYPAL", strategy);
            } else if (strategy instanceof CashOnDeliveryPaymentStrategy) {
                strategies.put("COD", strategy);
            }
        }
    }

    public PaymentResult processPayment(String paymentMethod, double amount, String paymentDetails, String customerEmail) {
        PaymentStrategy strategy = strategies.get(paymentMethod.toUpperCase());

        if (strategy == null) {
            return new PaymentResult(false, "Unsupported payment method: " + paymentMethod, "UNSUPPORTED_METHOD");
        }

        if (!strategy.isAvailable()) {
            return new PaymentResult(false, "Payment method temporarily unavailable: " + paymentMethod, "METHOD_UNAVAILABLE");
        }

        return strategy.processPayment(amount, paymentDetails, customerEmail);
    }

    public Map<String, String> getAvailablePaymentMethods() {
        Map<String, String> availableMethods = new HashMap<>();

        for (Map.Entry<String, PaymentStrategy> entry : strategies.entrySet()) {
            if (entry.getValue().isAvailable()) {
                availableMethods.put(entry.getKey(), entry.getValue().getPaymentMethodName());
            }
        }

        return availableMethods;
    }

    public boolean isPaymentMethodSupported(String paymentMethod) {
        return strategies.containsKey(paymentMethod.toUpperCase());
    }
}
