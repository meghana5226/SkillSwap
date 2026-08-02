package com.skillswap.service;

import com.skillswap.dto.AuditLogResponse;
import com.skillswap.entity.AuditLog;
import com.skillswap.entity.User;
import com.skillswap.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void record(User actor, String action, String targetType, UUID targetId, String details) {
        AuditLog log = AuditLog.builder()
                .actor(actor)
                .action(action)
                .targetType(targetType)
                .targetId(targetId)
                .details(details)
                .build();
        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> recent() {
        return auditLogRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(l -> new AuditLogResponse(
                        l.getId(), l.getActor().getFullName(), l.getActor().getEmail(),
                        l.getAction(), l.getTargetType(), l.getTargetId(), l.getDetails(), l.getCreatedAt()
                ))
                .toList();
    }
}
