package me.suhyun.soj.domain.problem.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class ProblemErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
) : ErrorCode {
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "PROBLEM_001", "Problem not found"),
    PROBLEM_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "PROBLEM_002", "Problem already deleted"),
    INVALID_DIFFICULTY(HttpStatus.BAD_REQUEST, "PROBLEM_003", "Invalid difficulty level")
}
