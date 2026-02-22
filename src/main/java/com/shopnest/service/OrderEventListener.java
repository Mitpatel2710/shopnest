package com.shopnest.service;

import com.shopnest.model.OrderEvent;

// Observer interface — every listener must implement this
public interface OrderEventListener {
    void onOrderEvent(OrderEvent event);
    String getListenerName();
}