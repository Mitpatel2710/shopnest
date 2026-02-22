package com.shopnest.service;

import com.shopnest.model.OrderEvent;

public class EmailNotificationListener implements OrderEventListener {

    @Override
    public void onOrderEvent(OrderEvent event) {
        String subject = switch (event.getType()) {
            case ORDER_PLACED    -> "Your order has been placed!";
            case ORDER_CONFIRMED -> "Your order is confirmed!";
            case ORDER_SHIPPED   -> "Your order is on the way!";
            case ORDER_DELIVERED -> "Your order has been delivered!";
            case ORDER_CANCELLED -> "Your order has been cancelled.";
        };
        System.out.println("  📧 Email → " +
                event.getOrder().getUser().getEmail() +
                " | Subject: " + subject);
    }

    @Override
    public String getListenerName() { return "EmailNotificationListener"; }
}