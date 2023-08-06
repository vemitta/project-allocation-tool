package com.theja.projectallocationservice.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class DBUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String email;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "users_skills",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<DBSkill> skills;
    @OneToMany(mappedBy="interviewer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBInterview> interviews;
    @OneToMany(mappedBy="candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBApplication> applications;
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<DBAuditLog> actions;
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    private List<DBOpening> openings;
}
