package com.theja.projectallocationservice.exceptions;

public class OpeningNotFoundException extends ResourceNotFoundException {
    public OpeningNotFoundException(Long id) {
        super("Opening not found with ID: " + id);
    }
}

