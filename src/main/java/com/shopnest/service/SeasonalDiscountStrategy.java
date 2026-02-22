package com.shopnest.service;

import com.shopnest.model.BaseProduct;
import com.shopnest.model.User;
import java.time.MonthDay;
import java.time.LocalDate;

public class SeasonalDiscountStrategy implements DiscountStrategy {

    private final double discountPercent;
    private final String seasonName;

    public SeasonalDiscountStrategy(String seasonName, double discountPercent) {
        this.seasonName      = seasonName;
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculateDiscountPercent(BaseProduct product, User user) {
        // Only apply if product is eligible for discount
        return product.isEligibleForDiscount() ? discountPercent : 0.0;
    }

    @Override
    public String getStrategyName() { return seasonName + " Sale (" + discountPercent + "% off)"; }
}