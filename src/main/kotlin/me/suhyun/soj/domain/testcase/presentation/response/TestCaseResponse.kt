package me.suhyun.soj.domain.testcase.presentation.response

import me.suhyun.soj.domain.testcase.domain.model.TestCase
import java.time.LocalDateTime

data class TestCaseResponse(
    val id: Long,
    val problemId: Long,
    val initSql: String?,
    val answer: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(testCase: TestCase): TestCaseResponse {
            return TestCaseResponse(
                id = testCase.id!!,
                problemId = testCase.problemId,
                initSql = testCase.initSql,
                answer = testCase.answer,
                createdAt = testCase.createdAt
            )
        }
    }
}
