package com.shopnest.exception;

public class UnauthorizedAccessException extends UserException {

    public UnauthorizedAccessException(String userId, String action) {
        super(ErrorCode.USER_UNAUTHORIZED,
                "User " + userId + " is not authorized to: " + action,
                userId, 403);
    }
}