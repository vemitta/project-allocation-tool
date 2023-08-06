package com.theja.projectallocationservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private String email;
    private List<Skill> skills;
    @JsonIgnore
    private List<Interview> interviews;
    @JsonIgnore
    private List<Application> applications;
    @JsonIgnore
    private List<AuditLog> actions;
    @JsonIgnore
    private List<Opening> openings;
}
