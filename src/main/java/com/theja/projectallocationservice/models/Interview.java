package com.theja.projectallocationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interview {
    private Long id;
    private String title;
    private PublicUser interviewer;
    private InterviewStatus status;
    private Date scheduledTime;
    @JsonIgnore
    private Application application;
}
