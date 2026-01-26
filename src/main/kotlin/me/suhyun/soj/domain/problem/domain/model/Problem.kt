package me.suhyun.soj.domain.problem.domain.model

import me.suhyun.soj.domain.problem.domain.entity.ProblemEntity
import java.time.LocalDateTime

data class Problem(
    val id: Long?,
    val title: String,
    val description: String,
    val schemaSql: String,
    val difficulty: Int,
    val timeLimit: Int,
    val isOrderSensitive: Boolean,
    val solvedCount: Int,
    val submissionCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {
    companion object {
        fun from(entity: ProblemEntity) = Problem(
            id = entity.id.value,
            title = entity.title,
            description = entity.description,
            schemaSql = entity.schemaSql,
            difficulty = entity.difficulty,
            timeLimit = entity.timeLimit,
            isOrderSensitive = entity.isOrderSensitive,
            solvedCount = entity.solvedCount,
            submissionCount = entity.submissionCount,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt
        )
    }
}
