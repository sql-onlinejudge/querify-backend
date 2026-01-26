package me.suhyun.soj.domain.submission.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class SubmissionErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 제출입니다."),
}
