package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.DBAuditLog;
import com.theja.projectallocationservice.models.DBOpening;
import com.theja.projectallocationservice.repositories.AuditLogRepository;
import com.theja.projectallocationservice.repositories.OpeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    public Page<DBAuditLog> getAllAuditLogs(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return auditLogRepository.findAll(pageRequest);
    }

    public List<DBAuditLog> getAllAuditLogsForUser(Long userId) {
        return auditLogRepository.findByUserId(userId);
    }

    public DBAuditLog getAuditLogById(Long id) {
        Optional<DBAuditLog> auditLog = auditLogRepository.findById(id);
        return auditLog.orElse(null);
    }

    public DBAuditLog createAuditLog(DBAuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }
}
