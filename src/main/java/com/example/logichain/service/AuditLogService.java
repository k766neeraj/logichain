package com.example.logichain.service;

import com.example.logichain.model.AuditAction;
import com.example.logichain.model.AuditLog;
import com.example.logichain.repository.AuditLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository){
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(AuditAction action, String entityType, int entityID){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityID);

        auditLogRepository.save(auditLog);
    }

}
