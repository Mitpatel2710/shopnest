package com.shopnest.model;

public interface Shippable {

    double calculateShippingCost();

    // Default - standard delivery days
    default int getEstimatedDeliveryDays(){
        return 5;
    }

    // Does this product need special packaging?
    default boolean requiresSpecialPackaging(){
        return false;
    }
}
