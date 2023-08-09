package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class DBProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String details;
    @ManyToMany
    @JoinTable(name = "users_projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<DBUser> allocatedUsers;
    @OneToMany(mappedBy="project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBOpening> openings;
}

