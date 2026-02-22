package com.shopnest.service;

import com.shopnest.model.Order;

public interface PaymentProcessor {

    // Process the payment — returns transaction ID
    String processPayment(Order order, double amount);

    // Refund a transaction
    boolean refund(String transactionId, double amount);

    // Payment method name
    String getPaymentMethodName();

    // Validate payment details before processing
    default boolean validate(double amount) {
        return amount > 0;
    }
}