package com.shopnest.exception;

public class ProductNotFoundException extends ProductException {

    public ProductNotFoundException(String productId) {
        super(ErrorCode.PRODUCT_NOT_FOUND,
                "Product not found with ID: " + productId,
                productId);
    }

    public ProductNotFoundException(String field, String value) {
        super(ErrorCode.PRODUCT_NOT_FOUND,
                "Product not found with " + field + ": " + value,
                value);
    }
}