package com.maimai.billingcalculationengine.common.constants;

public class MessageConstants {
    private MessageConstants() {}

    public static final class Auth {
        private Auth() {}

        public static final String INVALID_CREDENTIALS = "Invalid email or password";
        public static final String ACCOUNT_INACTIVE = "This account is inactive.";
        public static final String LOGIN_SUCCESS = "Successfully logged in";
        public static final String LOGOUT_SUCCESS = "Successfully logged out";
        public static final String MISSING_HEADER = "Authorization header is missing";
        public static final String INVALID_SCHEME = "Invalid authorization scheme. Expected 'Bearer'";
        public static final String EMPTY_TOKEN = "Token is empty";
    }

    public static final class User {
        public static final String USER_NOT_FOUND = "User not found";

        private User() {}

        public static final String EMAIL_EXISTS = "An account with this email already exists";
        public static final String ACCOUNT_CREATED = "Account created successfully";
    }

    public static final class Validation {
        private Validation() {}

        public static final String EMAIL_INVALID = "Please enter a valid email address";
        public static final String PASSWORD_WEAK = "Password must be at least 6 characters long and contain letters, numbers, and special characters";
        public static final String PASSWORD_MISMATCH = "Passwords do not match";
    }
}
