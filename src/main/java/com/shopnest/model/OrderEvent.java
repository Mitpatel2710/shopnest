package com.shopnest.model;

import java.time.LocalDateTime;

// Event object — carries data about what happened
public class OrderEvent {

    public enum Type {
        ORDER_PLACED, ORDER_CONFIRMED, ORDER_SHIPPED,
        ORDER_DELIVERED, ORDER_CANCELLED
    }

    private final Type type;
    private final Order order;
    private final LocalDateTime occurredAt;

    public OrderEvent(Type type, Order order) {
        this.type       = type;
        this.order      = order;
        this.occurredAt = LocalDateTime.now();
    }

    public Type getType()               { return type; }
    public Order getOrder()             { return order; }
    public LocalDateTime getOccurredAt(){ return occurredAt; }

    @Override
    public String toString() {
        return "OrderEvent{type=" + type +
                ", orderId='" + order.getId() + '\'' +
                ", at=" + occurredAt + "}";
    }
}