package me.suhyun.soj.domain.admin.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class AdminErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "Invalid email or password")
}
