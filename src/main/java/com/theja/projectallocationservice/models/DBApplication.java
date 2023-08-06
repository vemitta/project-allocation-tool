package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class DBApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @Column(name = "applied_at")
    private Date appliedAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id")
    private DBUser candidate;
    @ManyToOne(optional = false)
    @JoinColumn(name="opening_id")
    private DBOpening opening;
    @OneToMany(mappedBy="application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBInterview> interviews;
}

