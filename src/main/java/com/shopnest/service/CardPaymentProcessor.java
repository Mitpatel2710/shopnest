package com.shopnest.service;

import com.shopnest.model.Order;
import java.util.UUID;

public class CardPaymentProcessor implements PaymentProcessor {

    private final String cardNetwork; // VISA, MASTERCARD, RUPAY

    public CardPaymentProcessor(String cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    @Override
    public String processPayment(Order order, double amount) {
        System.out.println("  💳 Processing " + cardNetwork +
                " card payment of ₹" + amount +
                " for order " + order.getId());
        return "CARD-TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.println("  🔄 Refunding ₹" + amount + " for CARD txn: " + transactionId);
        return true;
    }

    @Override
    public String getPaymentMethodName() { return "CARD-" + cardNetwork; }
}