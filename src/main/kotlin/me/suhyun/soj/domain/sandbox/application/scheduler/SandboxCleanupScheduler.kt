package me.suhyun.soj.domain.sandbox.application.scheduler

import me.suhyun.soj.domain.sandbox.domain.model.SandboxStatus
import me.suhyun.soj.domain.sandbox.domain.repository.SandboxSessionRepository
import me.suhyun.soj.domain.sandbox.infrastructure.SandboxSchemaManager
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SandboxCleanupScheduler(
    private val sandboxSessionRepository: SandboxSessionRepository,
    private val sandboxSchemaManager: SandboxSchemaManager
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelay = 600_000)
    @Transactional
    fun cleanupExpiredSessions() {
        val expired = sandboxSessionRepository.findExpiredActive()
        expired.forEach { session ->
            runCatching { sandboxSchemaManager.dropSchema(session.schemaName) }
                .onFailure { log.warn("Failed to drop schema for session {}: {}", session.sessionKey, it.message) }
            sandboxSessionRepository.updateStatus(session.id!!, SandboxStatus.EXPIRED)
            log.info("Expired sandbox session: {}", session.sessionKey)
        }
    }
}
