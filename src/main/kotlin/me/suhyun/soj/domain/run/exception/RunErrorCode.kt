package me.suhyun.soj.domain.run.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class RunErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    RUN_NOT_FOUND(HttpStatus.NOT_FOUND, "실행 기록을 찾을 수 없습니다"),
}
