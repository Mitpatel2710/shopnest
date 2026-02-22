package com.shopnest.exception;

import com.shopnest.model.OrderStatus;

public class InvalidOrderStateException extends OrderException {

    private final OrderStatus currentStatus;
    private final OrderStatus attemptedStatus;

    public InvalidOrderStateException(String orderId,
                                      OrderStatus currentStatus,
                                      OrderStatus attemptedStatus) {
        super(ErrorCode.ORDER_INVALID_STATE,
                String.format("Cannot transition order %s from %s to %s",
                        orderId, currentStatus, attemptedStatus),
                orderId);
        this.currentStatus   = currentStatus;
        this.attemptedStatus = attemptedStatus;
    }

    public OrderStatus getCurrentStatus()   { return currentStatus; }
    public OrderStatus getAttemptedStatus() { return attemptedStatus; }
}