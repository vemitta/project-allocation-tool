package com.theja.projectallocationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Opening {
    private Long id;
    private String title;
    private String details;
    private Integer level;
    private String location;
    private OpeningStatus status;
    private PublicUser recruiter;
    private Project project;
    private List<Application> applications;
    private List<Skill> skills;
}
