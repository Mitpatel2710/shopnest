package com.shopnest.service;

import com.shopnest.model.BaseProduct;
import com.shopnest.model.User;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscountPercent(BaseProduct product, User user) {
        return 0.0; // no discount
    }

    @Override
    public String getStrategyName() { return "No Discount"; }
}