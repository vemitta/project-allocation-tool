package com.theja.projectallocationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Long id;
    private String title;
    private String details;
    private Collection<User> allocatedUsers;
    @JsonIgnore
    private List<Opening> openings;
}

