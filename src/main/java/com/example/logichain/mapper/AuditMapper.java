package com.example.logichain.mapper;

import com.example.logichain.dto.AuditLogDTO;
import com.example.logichain.model.AuditLog;

public class AuditMapper {
    public static AuditLogDTO toDTO(AuditLog auditLog){
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setUsername(auditLog.getUsername());
        dto.setAction(auditLog.getAction().name());
        dto.setDescription(auditLog.getDescription());
        dto.setTimestamp(auditLog.getTimestamp());
        dto.setEntityId(auditLog.getEntityId());
        dto.setEntityType(auditLog.getEntityType().name());
        return dto;
    }
}
