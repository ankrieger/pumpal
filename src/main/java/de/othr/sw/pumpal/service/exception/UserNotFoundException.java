package de.othr.sw.pumpal.service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User doesn't exists with email: " + email);
    }
}
