package com.shopnest.exception;

public class DuplicateEmailException extends UserException {

    private final String email;

    public DuplicateEmailException(String email) {
        super(ErrorCode.USER_DUPLICATE_EMAIL,
                "Email already registered: " + email,
                null, 409);
        this.email = email;
    }

    public String getEmail() { return email; }
}