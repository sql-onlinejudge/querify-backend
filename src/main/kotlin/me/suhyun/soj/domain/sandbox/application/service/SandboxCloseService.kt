package me.suhyun.soj.domain.sandbox.application.service

import me.suhyun.soj.domain.sandbox.domain.model.SandboxStatus
import me.suhyun.soj.domain.sandbox.domain.repository.SandboxSessionRepository
import me.suhyun.soj.domain.sandbox.exception.SandboxErrorCode
import me.suhyun.soj.domain.sandbox.infrastructure.SandboxSchemaManager
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SandboxCloseService(
    private val sandboxSessionRepository: SandboxSessionRepository,
    private val sandboxSchemaManager: SandboxSchemaManager
) {

    @Transactional
    fun close(sessionKey: String, userId: UUID) {
        val session = sandboxSessionRepository.findBySessionKey(sessionKey)
            ?: throw BusinessException(SandboxErrorCode.SANDBOX_SESSION_NOT_FOUND)

        if (!session.isActive()) throw BusinessException(SandboxErrorCode.SANDBOX_SESSION_NOT_ACTIVE)
        if (!session.isOwnedBy(userId.toString())) throw BusinessException(SandboxErrorCode.SANDBOX_FORBIDDEN)

        sandboxSchemaManager.dropSchema(session.schemaName)
        sandboxSessionRepository.updateStatus(session.id!!, SandboxStatus.CLOSED)
    }
}
