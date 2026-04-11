package me.suhyun.soj.domain.sandbox.domain.repository

import me.suhyun.soj.domain.sandbox.domain.model.SandboxSession
import me.suhyun.soj.domain.sandbox.domain.model.SandboxStatus

interface SandboxSessionRepository {
    fun save(session: SandboxSession): SandboxSession
    fun findBySessionKey(sessionKey: String): SandboxSession?
    fun findByUserId(userId: String): List<SandboxSession>
    fun findExpiredActive(): List<SandboxSession>
    fun updateStatus(id: Long, status: SandboxStatus)
}
