package com.shopnest.exception;

import java.time.LocalDateTime;

// Base exception — all ShopNest exceptions extend this
// Extends RuntimeException — unchecked, no need to declare in method signature
public class ShopNestException extends RuntimeException {

    private final ErrorCode errorCode;
    private final int httpStatus;
    private final LocalDateTime timestamp;

    // ── Constructors ──────────────────────────────────
    public ShopNestException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode  = errorCode;
        this.httpStatus = 500;
        this.timestamp  = LocalDateTime.now();
    }

    public ShopNestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode  = errorCode;
        this.httpStatus = 500;
        this.timestamp  = LocalDateTime.now();
    }

    public ShopNestException(ErrorCode errorCode, String message, int httpStatus) {
        super(message);
        this.errorCode  = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp  = LocalDateTime.now();
    }

    // Exception chaining — wraps original cause
    public ShopNestException(ErrorCode errorCode, String message,
                             int httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode  = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp  = LocalDateTime.now();
    }

    // ── Getters ───────────────────────────────────────
    public ErrorCode getErrorCode()       { return errorCode; }
    public int getHttpStatus()            { return httpStatus; }
    public LocalDateTime getTimestamp()   { return timestamp; }

    @Override
    public String toString() {
        return "ShopNestException{" +
                "errorCode=" + errorCode +
                ", httpStatus=" + httpStatus +
                ", message='" + getMessage() + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}