package com.shopnest.exception;

public class InvalidPaymentMethodException extends PaymentException {

    private final String method;

    public InvalidPaymentMethodException(String method, String orderId) {
        super(ErrorCode.PAYMENT_INVALID_METHOD,
                "Invalid payment method: " + method +
                        ". Accepted: UPI, CARD, NETBANKING, WALLET",
                orderId, 0);
        this.method = method;
    }

    public String getMethod() { return method; }
}