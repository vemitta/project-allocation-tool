package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.exceptions.ResourceNotFoundException;
import com.theja.projectallocationservice.models.DBAuditComment;
import com.theja.projectallocationservice.repositories.AuditCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditCommentService {
    @Autowired
    private AuditCommentRepository auditCommentRepository;

    public List<DBAuditComment> getAllAuditComments() {
        return auditCommentRepository.findAll();
    }

    public DBAuditComment getAuditCommentById(Long id) {
        return auditCommentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit comment not found"));
    }

    public List<DBAuditComment> getAuditCommentsByAuditLogId(Long auditLogId) {
        return auditCommentRepository.findByAuditLogId(auditLogId);
    }

    public DBAuditComment createAuditComment(DBAuditComment auditComment) {
        return auditCommentRepository.save(auditComment);
    }
}
