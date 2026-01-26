package me.suhyun.soj.domain.problem.presentation.response

import me.suhyun.soj.domain.problem.domain.model.Problem
import java.time.LocalDateTime

data class ProblemDetailResponse(
    val id: Long,
    val title: String,
    val description: String,
    val schemaSql: String,
    val difficulty: Int,
    val timeLimit: Int,
    val isOrderSensitive: Boolean,
    val solvedCount: Int,
    val submissionCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(problem: Problem): ProblemDetailResponse {
            return ProblemDetailResponse(
                id = problem.id!!,
                title = problem.title,
                description = problem.description,
                schemaSql = problem.schemaSql,
                difficulty = problem.difficulty,
                timeLimit = problem.timeLimit,
                isOrderSensitive = problem.isOrderSensitive,
                solvedCount = problem.solvedCount,
                submissionCount = problem.submissionCount,
                createdAt = problem.createdAt,
                updatedAt = problem.updatedAt
            )
        }
    }
}
