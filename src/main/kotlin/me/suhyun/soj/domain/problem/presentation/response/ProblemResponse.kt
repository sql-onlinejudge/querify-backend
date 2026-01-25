package me.suhyun.soj.domain.problem.presentation.response

import me.suhyun.soj.domain.problem.domain.model.Problem
import java.time.LocalDateTime

data class ProblemResponse(
    val id: Long,
    val title: String,
    val difficulty: Int,
    val solvedCount: Int,
    val submissionCount: Int,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(problem: Problem) = ProblemResponse(
            id = problem.id!!,
            title = problem.title,
            difficulty = problem.difficulty,
            solvedCount = problem.solvedCount,
            submissionCount = problem.submissionCount,
            createdAt = problem.createdAt
        )
    }
}
