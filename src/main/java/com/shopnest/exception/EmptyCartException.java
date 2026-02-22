package com.shopnest.exception;

public class EmptyCartException extends OrderException {

    public EmptyCartException(String userId) {
        super(ErrorCode.ORDER_EMPTY_CART,
                "Cannot place order — cart is empty for user: " + userId,
                null);
    }
}