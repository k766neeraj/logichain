package com.example.logichain.service;

import com.example.logichain.dto.ApiResponse;
import com.example.logichain.dto.AuditLogDTO;
import com.example.logichain.mapper.AuditMapper;
import com.example.logichain.model.AuditAction;
import com.example.logichain.model.AuditLog;
import com.example.logichain.model.EntityType;
import com.example.logichain.repository.AuditLogRepository;
import com.example.logichain.specification.AuditSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }

    public void logAction(String username, AuditAction action, EntityType entityType, int entityID, String desciption){
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityID);
        auditLog.setDescription(desciption);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }

    public ApiResponse<List<AuditLogDTO>> getAllLogs(
            int page, int size, String sortBy, String username, String action, String entityType, LocalDate fromDate, LocalDate toDate
            ) {
        page = Math.max(page,0);
        size = Math.max(size,1);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        AuditAction auditAction = null;

        if(action !=null && !action.isBlank()){
            auditAction = AuditAction.valueOf(action.toUpperCase());
        }

        EntityType entity = null;

        if(entityType !=null && !entityType.isBlank()){
            entity = EntityType.valueOf(entityType.toUpperCase());
        }

        Specification<AuditLog> spec = Specification.where(
                AuditSpecification.hasUsername(username)
        ).and(
                AuditSpecification.hasAction(auditAction)
        ).and(
                AuditSpecification.hasEntityType(entity)
        ).and(
                AuditSpecification.betweenDates(fromDate,toDate)
        );

        Page<AuditLog> auditPage = auditLogRepository.findAll(spec,pageable);

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
