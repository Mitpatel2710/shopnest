package com.shopnest.service;

import com.shopnest.exception.InvalidPaymentMethodException;
import com.shopnest.model.PaymentMethod;

// Factory Pattern — caller asks for a processor, Factory decides which to create
// Caller never uses 'new UpiPaymentProcessor()' directly
// Adding new payment method = add one class + one case here. Nothing else changes.
public class PaymentProcessorFactory {

    // Private constructor — static factory, no instances
    private PaymentProcessorFactory() {}

    public static PaymentProcessor getProcessor(PaymentMethod method, String orderId) {
        return switch (method) {
            case UPI        -> new UpiPaymentProcessor("shopnest@upi");
            case CARD       -> new CardPaymentProcessor("VISA");
            case NETBANKING -> new UpiPaymentProcessor("shopnest@netbanking"); // reuse
            case WALLET     -> new UpiPaymentProcessor("shopnest@wallet");
            case COD        -> new CodPaymentProcessor();
            default         -> throw new InvalidPaymentMethodException(method.name(), orderId);
        };
    }

    // Overload — get processor by string (useful when reading from request)
    public static PaymentProcessor getProcessor(String method, String orderId) {
        try {
            return getProcessor(PaymentMethod.valueOf(method.toUpperCase()), orderId);
        } catch (IllegalArgumentException e) {
            throw new InvalidPaymentMethodException(method, orderId);
        }
    }
}