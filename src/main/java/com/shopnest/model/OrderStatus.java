package com.shopnest.model;

public enum OrderStatus {
    PENDING,        // order placed, payment not confirmed
    CONFIRMED,      // payment confirmed
    PROCESSING,     // being packed
    SHIPPED,        // out for delivery
    DELIVERED,      // reached customer
    CANCELLED,      // canceled before shipping
    REFUNDED        // money returned
}
