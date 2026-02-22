package com.shopnest.service;

import com.shopnest.model.BaseProduct;
import com.shopnest.model.User;
import com.shopnest.model.UserRole;

public class LoyaltyDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscountPercent(BaseProduct product, User user) {
        // Sellers and admins get better discounts
        if (user.getRole() == UserRole.ADMIN)  return 20.0;
        if (user.getRole() == UserRole.SELLER) return 15.0;
        return 5.0; // regular customer loyalty discount
    }

    @Override
    public String getStrategyName() { return "Loyalty Discount"; }
}