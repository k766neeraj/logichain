package com.example.logichain.specification;

import com.example.logichain.model.AuditAction;
import com.example.logichain.model.AuditLog;
import com.example.logichain.model.EntityType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class AuditSpecification {
    public static Specification<AuditLog> hasUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank()) {
                return null;
            }
            return cb.equal(
                    root.get("username"),
                    username
            );
        };
    }

    public static Specification<AuditLog> hasAction(AuditAction action) {
        return (root, query, cb) -> {
            if (action == null) {
                return null;
            }
            return cb.equal(
                    root.get("action"),
                    action
            );
        };
    }

    public static Specification<AuditLog> hasEntityType(EntityType entityType) {
        return (root, query, cb) -> {
            if (entityType == null) {
                return null;
            }
            return cb.equal(
                    root.get("entityType"),
                    entityType
            );
        };
    }

    public static Specification<AuditLog> betweenDates(LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) -> {
            if (fromDate == null || toDate == null) {
                return null;
            }
            return cb.between(
                    root.get("timestamp"),
                    fromDate.atStartOfDay(),
                    toDate.atTime(LocalTime.MAX)
            );
        };

    }
}
