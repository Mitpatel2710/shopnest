package com.shopnest.exception;

public class UserException extends ShopNestException {

    private final String userId;

    public UserException(ErrorCode errorCode, String message,
                         String userId, int httpStatus) {
        super(errorCode, message, httpStatus);
        this.userId = userId;
    }

    public String getUserId() { return userId; }
}