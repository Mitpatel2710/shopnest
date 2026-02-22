package com.shopnest.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// T = Type placeholder — can be Product, Order, User, List<Product>, anything
public class ApiResponse<T> {

    // ── Fields ────────────────────────────────────────────
    private final boolean success;
    private final String message;
    private final T data;                       // T makes this work for ANY type
    private final LocalDateTime timestamp;
    private final List<String> errors;
    private final int statusCode;

    // ── Private constructor — force use of static factory methods ──
    private ApiResponse(boolean success, String message, T data,
                        int statusCode, List<String> errors) {
        this.success    = success;
        this.message    = message;
        this.data       = data;
        this.statusCode = statusCode;
        this.errors     = errors != null ? errors : new ArrayList<>();
        this.timestamp  = LocalDateTime.now();
    }

    // ── Static factory methods — clean API for callers ────

    // Success with data
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, 200, null);
    }

    // Success with custom message and data
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, 200, null);
    }

    // Success with status code (e.g. 201 Created)
    public static <T> ApiResponse<T> success(String message, T data, int statusCode) {
        return new ApiResponse<>(true, message, data, statusCode, null);
    }

    // Created — 201
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(true, message, data, 201, null);
    }

    // Error with message
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, 400, null);
    }

    // Error with status code
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(false, message, null, statusCode, null);
    }

    // Error with multiple validation errors
    public static <T> ApiResponse<T> validationError(List<String> errors) {
        return new ApiResponse<>(false, "Validation failed", null, 422, errors);
    }

    // Not found — 404
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, message, null, 404, null);
    }

    // Unauthorized — 401
    public static <T> ApiResponse<T> unauthorized() {
        return new ApiResponse<>(false, "Unauthorized access", null, 401, null);
    }

    // ── Getters ───────────────────────────────────────────
    public boolean isSuccess()          { return success; }
    public String getMessage()          { return message; }
    public T getData()                  { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<String> getErrors()     { return Collections.unmodifiableList(errors); }
    public int getStatusCode()          { return statusCode; }
    public boolean hasErrors()          { return !errors.isEmpty(); }
    public boolean hasData()            { return data != null; }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", hasData=" + hasData() +
                ", timestamp=" + timestamp +
                '}';
    }
}