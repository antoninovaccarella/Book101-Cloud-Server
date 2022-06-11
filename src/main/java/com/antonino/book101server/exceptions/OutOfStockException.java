package com.antonino.book101server.exceptions;

public class OutOfStockException extends Exception{
    private final String error;

    public OutOfStockException(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return this.error;
    }
}
