package com.shopnest.exception;

public class OrderNotFoundException extends OrderException {

    public OrderNotFoundException(String orderId) {
        super(ErrorCode.ORDER_NOT_FOUND,
                "Order not found with ID: " + orderId,
                orderId);
    }
}