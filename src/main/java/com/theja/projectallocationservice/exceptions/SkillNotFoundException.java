package com.theja.projectallocationservice.exceptions;

public class SkillNotFoundException extends  ResourceNotFoundException{
    public SkillNotFoundException(String message) {
        super(message);
    }
}
