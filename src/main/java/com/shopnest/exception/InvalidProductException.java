package com.shopnest.exception;

import java.util.List;

public class InvalidProductException extends ProductException {

    private final List<String> validationErrors;

    public InvalidProductException(String productId, List<String> validationErrors) {
        super(ErrorCode.PRODUCT_INVALID,
                "Invalid product data: " + String.join(", ", validationErrors),
                productId);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() { return validationErrors; }
}