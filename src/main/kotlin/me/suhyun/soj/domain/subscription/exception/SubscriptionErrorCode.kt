package me.suhyun.soj.domain.subscription.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class SubscriptionErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구독 정보를 찾을 수 없습니다.")
}
