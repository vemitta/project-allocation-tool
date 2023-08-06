package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBAuditLog;
import com.theja.projectallocationservice.models.DBOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<DBAuditLog, Long> {

    List<DBAuditLog> findByUserId(Long userId);
}
