package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "openings")
public class DBOpening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String details;
    @NotNull(message = "Experience level is required for an opening")
    private Integer level;
    @NotNull
    private String location;
    @Enumerated(EnumType.STRING)
    private OpeningStatus status;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private DBUser recruiter;
    @ManyToOne(optional = false)
    @JoinColumn(name="project_id")
    private DBProject project;
    @OneToMany(mappedBy="opening", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBApplication> applications = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "openings_skills",
            joinColumns = @JoinColumn(name = "opening_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @NotNull
    private List<DBSkill> skills;
}
