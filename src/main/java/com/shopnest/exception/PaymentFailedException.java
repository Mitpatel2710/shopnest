package com.shopnest.exception;

public class PaymentFailedException extends PaymentException {

    private final String reason;

    public PaymentFailedException(String orderId, double amount, String reason) {
        super(ErrorCode.PAYMENT_FAILED,
                String.format("Payment of ₹%.2f failed for order %s. Reason: %s",
                        amount, orderId, reason),
                orderId, amount);
        this.reason = reason;
    }

    public String getReason() { return reason; }
}