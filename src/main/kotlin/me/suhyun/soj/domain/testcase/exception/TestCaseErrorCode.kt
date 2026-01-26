package me.suhyun.soj.domain.testcase.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class TestCaseErrorCode(
    override val status: HttpStatus,
    override val message: String
) : ErrorCode {
    TEST_CASE_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 테스트 케이스입니다."),
}
