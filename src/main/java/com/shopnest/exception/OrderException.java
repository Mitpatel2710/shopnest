package com.shopnest.exception;

public class OrderException extends ShopNestException {

    private final String orderId;

    public OrderException(ErrorCode errorCode, String message, String orderId) {
        super(errorCode, message, 400);
        this.orderId = orderId;
    }

    public String getOrderId() { return orderId; }
}