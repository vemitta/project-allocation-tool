package com.theja.projectallocationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditComment {
    private Long id;
    private String comment;
    private AuditLog auditLog;
}
