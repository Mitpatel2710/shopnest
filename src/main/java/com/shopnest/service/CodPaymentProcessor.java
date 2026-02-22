package com.shopnest.service;

import com.shopnest.model.Order;
import java.util.UUID;

public class CodPaymentProcessor implements PaymentProcessor {

    @Override
    public String processPayment(Order order, double amount) {
        System.out.println("  💵 COD order placed for ₹" + amount +
                ". Payment on delivery for order " + order.getId());
        return "COD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.println("  🔄 COD refund initiated for ₹" + amount);
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "COD"; }

    @Override
    public boolean validate(double amount) {
        // COD limit — max ₹50,000
        return amount > 0 && amount <= 50000;
    }
}