package com.shopnest.model;

// Record — immutable data carrier, Java 16+
// Auto generates: constructor, getters, equals, hashCode, toString
public record ProductSummary(
        String id,
        String name,
        double price,
        String category,
        boolean available
) {
    // Compact constructor — add validation inside records
    public ProductSummary {
        if (id == null || id.isBlank())   throw new IllegalArgumentException("ID cannot be empty");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        if (price < 0)                    throw new IllegalArgumentException("Price cannot be negative");
    }

    // Custom method on record
    public String getDisplayLabel() {
        return String.format("[%s] %s — ₹%.2f %s",
                category, name, price, available ? "✅" : "❌");
    }
}