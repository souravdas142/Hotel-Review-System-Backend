package com.nav.user.service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

//    extra properties that you want to manage
    public ResourceNotFoundException() {
        super("Resource not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
