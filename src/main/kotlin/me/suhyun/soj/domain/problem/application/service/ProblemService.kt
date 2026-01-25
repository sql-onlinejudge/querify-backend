package me.suhyun.soj.domain.problem.application.service

import me.suhyun.soj.domain.problem.domain.model.Problem
import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.problem.exception.ProblemErrorCode
import me.suhyun.soj.domain.problem.presentation.request.CreateProblemRequest
import me.suhyun.soj.domain.problem.presentation.request.UpdateProblemRequest
import me.suhyun.soj.global.dto.PageResponse
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ProblemService(
    private val problemRepository: ProblemRepository
) {

    @Transactional
    fun createProblem(request: CreateProblemRequest): Problem {
        val problem = Problem(
            id = null,
            title = request.title,
            description = request.description,
            schemaSql = request.schemaSql,
            difficulty = request.difficulty,
            timeLimit = request.timeLimit,
            isOrderSensitive = request.isOrderSensitive,
            solvedCount = 0,
            submissionCount = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            deletedAt = null
        )
        return problemRepository.save(problem)
    }

    @Transactional(readOnly = true)
    fun getProblem(id: Long): Problem {
        return problemRepository.findById(id)
            ?: throw BusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND)
    }

    @Transactional(readOnly = true)
    fun getProblemList(
        page: Int,
        size: Int,
        difficulty: Int?,
        keyword: String?,
        sortBy: String,
        sortDirection: String
    ): PageResponse<Problem> {
        val problems = problemRepository.findAll(
            page = page,
            size = size,
            difficulty = difficulty,
            keyword = keyword,
            sortBy = sortBy,
            sortDirection = sortDirection
        )
        val totalElements = problemRepository.countAll(difficulty, keyword)

        return PageResponse.of(
            content = problems,
            page = page,
            size = size,
            totalElements = totalElements
        )
    }

    @Transactional
    fun updateProblem(id: Long, request: UpdateProblemRequest): Problem {
        return problemRepository.update(
            id = id,
            title = request.title,
            description = request.description,
            schemaSql = request.schemaSql,
            difficulty = request.difficulty,
            timeLimit = request.timeLimit,
            isOrderSensitive = request.isOrderSensitive
        ) ?: throw BusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND)
    }

    @Transactional
    fun deleteProblem(id: Long) {
        val deleted = problemRepository.softDelete(id)
        if (!deleted) {
            throw BusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND)
        }
    }
}
