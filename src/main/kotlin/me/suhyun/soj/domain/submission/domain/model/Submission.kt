package me.suhyun.soj.domain.submission.domain.model

import me.suhyun.soj.domain.submission.domain.entity.SubmissionEntity
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import java.time.LocalDateTime
import java.util.UUID

data class Submission(
    val id: Long?,
    val problemId: Long,
    val userId: UUID,
    val query: String,
    val status: SubmissionStatus,
    val verdict: SubmissionVerdict?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {
    companion object {
        fun from(entity: SubmissionEntity) = Submission(
            id = entity.id.value,
            problemId = entity.problemId,
            userId = UUID.fromString(entity.userId),
            query = entity.query,
            status = entity.status,
            verdict = entity.verdict,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt
        )
    }
}
