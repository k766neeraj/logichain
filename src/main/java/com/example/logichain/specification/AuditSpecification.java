package com.example.logichain.specification;

import com.example.logichain.model.AuditLog;
import org.springframework.data.jpa.domain.Specification;

public class AuditSpecification {
    public static Specification<AuditLog> hasUsername(String username){
        return (root, query, cb) -> {
            if(username == null && username.isBlank()){
                return null;
            }
            return cb.equal(
                    root.get(username),
                    username
            );
        };
    }
}
