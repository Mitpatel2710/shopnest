package com.shopnest.exception;

public class PaymentException extends ShopNestException {

    private final String orderId;
    private final double amount;

    public PaymentException(ErrorCode errorCode, String message,
                            String orderId, double amount) {
        super(errorCode, message, 402);
        this.orderId = orderId;
        this.amount  = amount;
    }

    public String getOrderId() { return orderId; }
    public double getAmount()  { return amount; }
}