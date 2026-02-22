package com.shopnest.service;

import com.shopnest.model.BaseProduct;
import com.shopnest.model.User;

// Strategy Pattern — family of algorithms, interchangeable at runtime
// Each strategy = one way to calculate discount
// Adding new discount type = new class, nothing else changes
public interface DiscountStrategy {

    // Calculate discount percent for a product and user
    double calculateDiscountPercent(BaseProduct product, User user);

    // Human-readable strategy name
    String getStrategyName();

    // Default — apply strategy and return final price
    default double applyDiscount(BaseProduct product, User user) {
        double percent = calculateDiscountPercent(product, user);
        return product.getPriceAfterDiscount(percent);
    }
}