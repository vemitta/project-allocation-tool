package com.theja.projectallocationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    private Long id;
    private ApplicationStatus status;
    private Date appliedAt;
    private PublicUser candidate;
    private Opening opening;
    private List<Interview> interviews;
}

