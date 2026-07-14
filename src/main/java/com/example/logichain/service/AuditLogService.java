package com.example.logichain.service;

import com.example.logichain.dto.ApiResponse;
import com.example.logichain.dto.AuditLogDTO;
import com.example.logichain.mapper.AuditMapper;
import com.example.logichain.model.AuditAction;
import com.example.logichain.model.AuditLog;
import com.example.logichain.model.EntityType;
import com.example.logichain.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository){
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(AuditAction action, EntityType entityType, int entityID, String desciption){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityID);
        auditLog.setDescription(desciption);

        auditLogRepository.save(auditLog);
    }

    public ApiResponse<List<AuditLogDTO>> getAllLogs(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));


        Page<AuditLog> auditPage = auditLogRepository.findAll(pageable);

        List<AuditLogDTO> dtoList = new ArrayList<>();

        for(AuditLog log : auditPage.getContent()){
            dtoList.add(AuditMapper.toDTO(log));
        }
        return new ApiResponse<>(
                dtoList,
                auditPage.getNumber(),
                auditPage.getSize(),
                Math.toIntExact(auditPage.getTotalElements()),
        "Audit Logs fetched successfully"
                );
    }
}
