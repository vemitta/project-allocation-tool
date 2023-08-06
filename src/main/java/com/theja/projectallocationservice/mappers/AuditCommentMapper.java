package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.AuditComment;
import com.theja.projectallocationservice.models.DBAuditComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditCommentMapper {
    @Autowired
    private AuditLogMapper auditLogMapper;

    public List<AuditComment> entityToModel(List<DBAuditComment> dbAuditComments) {
        return dbAuditComments.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    public AuditComment entityToModel(DBAuditComment dbAuditComment) {
        return new AuditComment(dbAuditComment.getId(), dbAuditComment.getComment(), auditLogMapper.entityToModel(dbAuditComment.getAuditLog()));
    }
}
