package com.shopnest.model;

public interface Discountable {

    // Every product that is discounteable MUST implement this
    double getPriceAfterDiscount(double discountPercent);

    // Default method - shared logic, can be overridden
    default boolean isEligibleForDiscount(){
        return true;
    }

    // Constant - interfaces can have constants
    double MAX_DISCOUNT_PERCENT = 70.0;
}
