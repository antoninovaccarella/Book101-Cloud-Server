package com.antonino.book101server.exceptions;

public class GCPFileUploadException extends RuntimeException{

    public GCPFileUploadException(String message, Throwable t) {
        super(message, t);
    }
}
