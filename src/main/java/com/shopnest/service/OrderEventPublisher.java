package com.shopnest.service;

import com.shopnest.model.OrderEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Publisher — maintains list of listeners and notifies all of them
// This is exactly how Spring's ApplicationEventPublisher works
public class OrderEventPublisher {

    private final List<OrderEventListener> listeners = new ArrayList<>();

    // ── Register listeners ────────────────────────────────
    public void subscribe(OrderEventListener listener) {
        listeners.add(listener);
        System.out.println("  ✅ Subscribed: " + listener.getListenerName());
    }

    public void unsubscribe(OrderEventListener listener) {
        listeners.remove(listener);
        System.out.println("  ❌ Unsubscribed: " + listener.getListenerName());
    }

    // ── Publish event — notify ALL listeners ──────────────
    public void publish(OrderEvent event) {
        System.out.println("\n  🔔 Publishing: " + event.getType() +
                " for order " + event.getOrder().getId());
        listeners.forEach(listener -> listener.onOrderEvent(event));
    }

    public List<OrderEventListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    public int getListenerCount() { return listeners.size(); }
}