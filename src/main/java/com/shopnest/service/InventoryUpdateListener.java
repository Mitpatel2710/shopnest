package com.shopnest.service;

import com.shopnest.model.OrderEvent;

public class InventoryUpdateListener implements OrderEventListener {

    @Override
    public void onOrderEvent(OrderEvent event) {
        if (event.getType() == OrderEvent.Type.ORDER_PLACED) {
            System.out.println("  📦 Inventory updated for order: " +
                    event.getOrder().getId() + " — " +
                    event.getOrder().getOrderItems().size() + " item(s) deducted");
        }
        if (event.getType() == OrderEvent.Type.ORDER_CANCELLED) {
            System.out.println("  📦 Inventory restored for cancelled order: " +
                    event.getOrder().getId());
        }
    }

    @Override
    public String getListenerName() { return "InventoryUpdateListener"; }
}