package me.suhyun.soj.domain.auth.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class AuthErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    GOOGLE_AUTH_FAILED(HttpStatus.BAD_GATEWAY, "Failed to authenticate with Google"),
    GITHUB_AUTH_FAILED(HttpStatus.BAD_GATEWAY, "Failed to authenticate with GitHub")
}
