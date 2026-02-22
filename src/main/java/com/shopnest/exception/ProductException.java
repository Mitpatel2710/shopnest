package com.shopnest.exception;

// Mid-level — groups all product related exceptions
public class ProductException extends ShopNestException {

    private final String productId;

    public ProductException(ErrorCode errorCode, String message, String productId) {
        super(errorCode, message, 400);
        this.productId = productId;
    }

    public String getProductId() { return productId; }
}