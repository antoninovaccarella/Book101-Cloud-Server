package com.antonino.book101server.exceptions;

public class ProductNotFoundException extends Exception {
    private final String error;

    public ProductNotFoundException(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return this.error;
    }
}
