package dev.navneet.userservice.exceptions;

public class InvalidPasswordResetTokenException extends RuntimeException{
    public InvalidPasswordResetTokenException(String message) {
        super(message);
    }
}
