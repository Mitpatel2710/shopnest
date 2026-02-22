package com.shopnest.exception;

public class OutOfStockException extends ProductException {

    private final int requestedQuantity;
    private final int availableQuantity;

    public OutOfStockException(String productId,
                               int requestedQuantity,
                               int availableQuantity) {
        super(ErrorCode.PRODUCT_OUT_OF_STOCK,
                String.format("Insufficient stock for product %s. " +
                                "Requested: %d, Available: %d",
                        productId, requestedQuantity, availableQuantity),
                productId);
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public int getRequestedQuantity() { return requestedQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
}