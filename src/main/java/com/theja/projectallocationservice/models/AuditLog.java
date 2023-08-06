package com.theja.projectallocationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    private Long id;
    private String action;
    private Date loggedAt;
    private PublicUser user;
    private List<AuditComment> auditComments;
}
