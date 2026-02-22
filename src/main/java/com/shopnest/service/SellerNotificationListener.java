package com.shopnest.service;

import com.shopnest.model.OrderEvent;

public class SellerNotificationListener implements OrderEventListener {

    @Override
    public void onOrderEvent(OrderEvent event) {
        if (event.getType() == OrderEvent.Type.ORDER_PLACED ||
                event.getType() == OrderEvent.Type.ORDER_CANCELLED) {
            System.out.println("  🏪 Seller notified: order " +
                    event.getOrder().getId() + " → " + event.getType());
        }
    }

    @Override
    public String getListenerName() { return "SellerNotificationListener"; }
}