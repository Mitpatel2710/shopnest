package com.shopnest.exception;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String userId) {
        super(ErrorCode.USER_NOT_FOUND,
                "User not found with ID: " + userId,
                userId, 404);
    }

    public UserNotFoundException(String field, String value) {
        super(ErrorCode.USER_NOT_FOUND,
                "User not found with " + field + ": " + value,
                value, 404);
    }
}