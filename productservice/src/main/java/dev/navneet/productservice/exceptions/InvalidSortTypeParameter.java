package dev.navneet.productservice.exceptions;

public class InvalidSortTypeParameter extends RuntimeException {
    public InvalidSortTypeParameter(String message) {
        super(message);
    }
}
