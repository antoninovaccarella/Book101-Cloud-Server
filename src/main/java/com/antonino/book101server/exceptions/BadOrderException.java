package com.antonino.book101server.exceptions;

public class BadOrderException extends Exception {
    private final String error;

    public BadOrderException(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return this.error;
    }
}
