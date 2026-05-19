package me.suhyun.soj.domain.submission.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class FeedbackErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    PREMIUM_REQUIRED(HttpStatus.FORBIDDEN, "프리미엄 구독이 필요합니다."),
    FEEDBACK_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 피드백 생성에 실패했습니다.")
}
