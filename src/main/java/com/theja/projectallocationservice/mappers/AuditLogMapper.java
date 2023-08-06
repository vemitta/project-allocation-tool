package com.theja.projectallocationservice.mappers;

import com.theja.projectallocationservice.models.AuditLog;
import com.theja.projectallocationservice.models.DBAuditLog;
import com.theja.projectallocationservice.models.DBOpening;
import com.theja.projectallocationservice.models.Opening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditLogMapper {
    @Autowired
    private UserMapper userMapper;

    public List<AuditLog> entityToModel(List<DBAuditLog> dbAuditLogs) {
        return dbAuditLogs.stream().map(this::entityToModel).collect(Collectors.toList());
    }

    public AuditLog entityToModel(DBAuditLog dbAuditLog) {
        return new AuditLog(dbAuditLog.getId(), dbAuditLog.getAction(), dbAuditLog.getLoggedAt(), userMapper.entityToPublicModel(dbAuditLog.getUser()), null);
    }
}
