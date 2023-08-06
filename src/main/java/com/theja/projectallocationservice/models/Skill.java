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
public class Skill {
    private Long id;
    private String title;
    @JsonIgnore
    private List<User> users;
    @JsonIgnore
    private List<DBOpening> openings;
}
