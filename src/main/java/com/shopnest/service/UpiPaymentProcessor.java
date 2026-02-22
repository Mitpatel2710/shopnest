package com.shopnest.service;

import com.shopnest.model.Order;
import java.util.UUID;

public class UpiPaymentProcessor implements PaymentProcessor {

    private final String upiId;

    public UpiPaymentProcessor(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public String processPayment(Order order, double amount) {
        System.out.println("  💳 Processing UPI payment of ₹" + amount +
                " via " + upiId + " for order " + order.getId());
        // Simulate payment — returns transaction ID
        return "UPI-TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.println("  🔄 Refunding ₹" + amount + " for UPI txn: " + transactionId);
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "UPI"; }
}