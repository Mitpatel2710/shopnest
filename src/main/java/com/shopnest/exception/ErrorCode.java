package com.shopnest.exception;

public enum ErrorCode {

    // Product errors — 1xxx
    PRODUCT_NOT_FOUND           ("PROD-1001", "Product not found"),
    PRODUCT_OUT_OF_STOCK        ("PROD-1002", "Product is out of stock"),
    PRODUCT_DUPLICATE           ("PROD-1003", "Product already exists"),
    PRODUCT_INVALID             ("PROD-1004", "Invalid product data"),

    // Order errors — 2xxx
    ORDER_NOT_FOUND             ("ORD-2001",  "Order not found"),
    ORDER_INVALID_STATE         ("ORD-2002",  "Invalid order state transition"),
    ORDER_EMPTY_CART            ("ORD-2003",  "Cannot place order from empty cart"),

    // User errors — 3xxx
    USER_NOT_FOUND              ("USR-3001",  "User not found"),
    USER_DUPLICATE_EMAIL        ("USR-3002",  "Email already registered"),
    USER_UNAUTHORIZED           ("USR-3003",  "Unauthorized access"),

    // Payment errors — 4xxx
    PAYMENT_FAILED              ("PAY-4001",  "Payment processing failed"),
    PAYMENT_INVALID_METHOD      ("PAY-4002",  "Invalid payment method");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code           = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode()           { return code; }
    public String getDefaultMessage() { return defaultMessage; }

    @Override
    public String toString() {
        return "[" + code + "] " + defaultMessage;
    }
}