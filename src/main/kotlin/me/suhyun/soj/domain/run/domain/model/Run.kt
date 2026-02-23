package me.suhyun.soj.domain.run.domain.model

import me.suhyun.soj.domain.run.domain.model.enums.RunStatus
import java.time.LocalDateTime

data class Run(
    val id: Long?,
    val status: RunStatus,
    val query: String,
    val problemId: Long,
    val result: String?,
    val errorMessage: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
)
