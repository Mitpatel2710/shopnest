package com.shopnest.service;

import com.shopnest.model.BaseProduct;
import com.shopnest.model.User;
import java.util.Map;

public class CouponDiscountStrategy implements DiscountStrategy {

    // Valid coupons — code → discount percent
    private static final Map<String, Double> VALID_COUPONS = Map.of(
            "WELCOME10",  10.0,
            "SAVE20",     20.0,
            "FESTIVE30",  30.0,
            "FLAT50",     50.0
    );

    private final String couponCode;

    public CouponDiscountStrategy(String couponCode) {
        this.couponCode = couponCode.toUpperCase();
    }

    @Override
    public double calculateDiscountPercent(BaseProduct product, User user) {
        return VALID_COUPONS.getOrDefault(couponCode, 0.0);
    }

    @Override
    public String getStrategyName() { return "Coupon: " + couponCode; }

    public boolean isValidCoupon() {
        return VALID_COUPONS.containsKey(couponCode);
    }
}