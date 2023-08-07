package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.AuditLogMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditLogMapper auditLogMapper;

    @GetMapping("/audit-logs")
    public ResponseEntity<AuditLogResponse> getAllAuditLogs(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        Page<DBAuditLog> dbAuditLogs = auditLogService.getAllAuditLogs(pageSize, pageNumber);
        AuditLogResponse response = AuditLogResponse.builder()
                .auditLogs(auditLogMapper.entityToModel(dbAuditLogs.getContent()))
                .totalElements(dbAuditLogs.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }
}
