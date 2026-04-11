package me.suhyun.soj.domain.sandbox.presentation.response

import me.suhyun.soj.domain.sandbox.domain.model.SandboxStatus
import java.time.LocalDateTime

data class SandboxSessionResponse(
    val sessionKey: String,
    val schemaName: String,
    val extractedSql: String,
    val expiresAt: LocalDateTime,
    val status: SandboxStatus,
    val createdAt: LocalDateTime
)
