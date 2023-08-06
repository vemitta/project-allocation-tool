package com.theja.projectallocationservice.repositories;

import com.theja.projectallocationservice.models.DBAuditComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditCommentRepository extends JpaRepository<DBAuditComment, Long> {

    List<DBAuditComment> findByAuditLogId(Long auditLogId);
}
