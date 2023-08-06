package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interviews")
public class DBInterview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne(optional = false)
    @JoinColumn(name = "interviewer_id")
    private DBUser interviewer;
    @Enumerated(EnumType.STRING)
    private InterviewStatus status;
    private Date scheduledTime;
    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    private DBApplication application;
}
