package me.suhyun.soj.domain.problem.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class ProblemErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 문제입니다."),
}
