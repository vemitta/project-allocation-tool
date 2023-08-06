package com.theja.projectallocationservice.controllers;

import com.theja.projectallocationservice.mappers.AuditCommentMapper;
import com.theja.projectallocationservice.models.*;
import com.theja.projectallocationservice.repositories.AuditCommentRepository;
import com.theja.projectallocationservice.repositories.AuditLogRepository;
import com.theja.projectallocationservice.services.AuditCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuditCommentController {

    @Autowired
    private AuditCommentRepository auditCommentRepository;

    @Autowired
    private AuditCommentService auditCommentService;

    @Autowired
    private AuditCommentMapper auditCommentMapper;

    @GetMapping("/audit-logs/{auditLogId}/comments")
    public ResponseEntity<List<AuditComment>> getAuditCommentsByAuditLogId(@PathVariable Long auditLogId) {
        List<DBAuditComment> dbAuditComments = auditCommentService.getAuditCommentsByAuditLogId(auditLogId);
        return new ResponseEntity<>(auditCommentMapper.entityToModel(dbAuditComments), HttpStatus.OK);
    }
}
