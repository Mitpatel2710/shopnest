package com.shopnest.exception;

import com.shopnest.util.ApiResponse;

public class GlobalExceptionHandler {

    // Handle any ShopNest exception — single entry point
    public static <T> ApiResponse<T> handle(ShopNestException ex) {
        System.out.println("\n🔴 Exception caught by GlobalExceptionHandler");
        System.out.println("   Code    : " + ex.getErrorCode().getCode());
        System.out.println("   Message : " + ex.getMessage());
        System.out.println("   Status  : " + ex.getHttpStatus());
        System.out.println("   Time    : " + ex.getTimestamp());

        return ApiResponse.error(ex.getMessage(), ex.getHttpStatus());
    }

    // Handle product not found specifically — 404
    public static <T> ApiResponse<T> handle(ProductNotFoundException ex) {
        System.out.println("\n🟡 Product Not Found: " + ex.getProductId());
        return ApiResponse.notFound(ex.getMessage());
    }

    // Handle out of stock — 400 with extra info
    public static <T> ApiResponse<T> handle(OutOfStockException ex) {
        System.out.println("\n🟠 Out of Stock: requested=" +
                ex.getRequestedQuantity() + " available=" + ex.getAvailableQuantity());
        return ApiResponse.error(ex.getMessage(), 400);
    }

    // Handle unauthorized — 403
    public static <T> ApiResponse<T> handle(UnauthorizedAccessException ex) {
        System.out.println("\n🔴 Unauthorized: " + ex.getMessage());
        return ApiResponse.error(ex.getMessage(), 403);
    }

    // Handle payment failed — 402
    public static <T> ApiResponse<T> handle(PaymentFailedException ex) {
        System.out.println("\n💳 Payment Failed: " + ex.getReason());
        return ApiResponse.error(ex.getMessage(), 402);
    }

    // Handle any unexpected exception — 500
    public static <T> ApiResponse<T> handleUnexpected(Exception ex) {
        System.out.println("\n💥 Unexpected error: " + ex.getMessage());
        return ApiResponse.error("An unexpected error occurred", 500);
    }
}