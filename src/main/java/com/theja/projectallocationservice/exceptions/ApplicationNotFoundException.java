package com.theja.projectallocationservice.exceptions;

public class ApplicationNotFoundException extends ResourceNotFoundException {

        public ApplicationNotFoundException(Long id) {
            super("Application not found with ID: " + id);
        }
}
