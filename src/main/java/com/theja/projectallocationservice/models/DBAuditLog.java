package com.theja.projectallocationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "audit_logs")
public class DBAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action;
    @Column(name = "logged_at")
    private Date loggedAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private DBUser user;
    @OneToMany(mappedBy="auditLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBAuditComment> auditComments = new ArrayList<>();
}
