package com.theja.projectallocationservice.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

//    public ResourceNotFoundException(Long applicationId) {
//        super("Application not found with ID: " + applicationId);
//    }
}

