package com.shopnest.service;

import com.shopnest.model.BaseProduct;
import com.shopnest.model.User;

// Context class — holds and uses a DiscountStrategy
// Strategy can be swapped at runtime
public class DiscountService {

    private DiscountStrategy strategy;

    public DiscountService(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    // ── Swap strategy at runtime ──────────────────────────
    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public double getFinalPrice(BaseProduct product, User user) {
        double finalPrice  = strategy.applyDiscount(product, user);
        double discountPct = strategy.calculateDiscountPercent(product, user);
        System.out.println("  Strategy : " + strategy.getStrategyName());
        System.out.println("  Original : ₹" + product.getPrice());
        System.out.println("  Discount : " + discountPct + "%");
        System.out.println("  Final    : ₹" + finalPrice);
        return finalPrice;
    }

    public String getStrategyName() { return strategy.getStrategyName(); }
}