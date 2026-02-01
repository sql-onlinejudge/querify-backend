package me.suhyun.soj.domain.grading.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class GradingErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    GRADING_SERVER_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "채점 서버에 연결할 수 없습니다"),
    INVALID_SQL_SYNTAX(HttpStatus.BAD_REQUEST, "SQL 문법이 유효하지 않습니다"),
    FORBIDDEN_SQL_STATEMENT(HttpStatus.BAD_REQUEST, "SELECT 문만 허용됩니다"),
}
