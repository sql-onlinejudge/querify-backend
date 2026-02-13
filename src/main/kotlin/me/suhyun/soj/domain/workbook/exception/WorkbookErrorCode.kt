package me.suhyun.soj.domain.workbook.exception

import me.suhyun.soj.global.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class WorkbookErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : ErrorCode {
    WORKBOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 문제집입니다."),
    WORKBOOK_PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 문제집 문제입니다."),
    WORKBOOK_PROBLEM_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 문제집에 추가된 문제입니다.")
}
