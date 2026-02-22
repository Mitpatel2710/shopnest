package com.shopnest.exception;

public class DuplicateProductException extends ProductException {

    public DuplicateProductException(String productId) {
        super(ErrorCode.PRODUCT_DUPLICATE,
                "Product already exists with ID: " + productId,
                productId);
    }
}