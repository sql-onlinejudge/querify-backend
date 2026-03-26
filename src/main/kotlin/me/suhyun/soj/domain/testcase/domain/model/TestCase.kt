package me.suhyun.soj.domain.testcase.domain.model

import me.suhyun.soj.domain.testcase.domain.entity.TestCaseEntity
import java.time.LocalDateTime

data class TestCase(
    val id: Long?,
    val problemId: Long,
    val initSql: String?,
    val initMetadata: InitMetadata?,
    val answer: String,
    val answerMetadata: AnswerMetadata?,
    val isVisible: Boolean? = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {
    companion object {
        fun from(entity: TestCaseEntity) = TestCase(
            id = entity.id.value,
            problemId = entity.problemId,
            initSql = entity.initSql,
            initMetadata = null,
            answer = entity.answer,
            answerMetadata = null,
            isVisible = entity.isVisible,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt
        )
    }
}
