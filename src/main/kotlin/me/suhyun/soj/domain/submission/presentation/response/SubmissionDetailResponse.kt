package me.suhyun.soj.domain.submission.presentation.response

import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import me.suhyun.soj.domain.submission.domain.model.Submission
import java.time.LocalDateTime
import java.util.UUID

data class SubmissionDetailResponse(
    val id: Long,
    val problemId: Long,
    val status: SubmissionStatus,
    val verdict: SubmissionVerdict?,
    val createdAt: LocalDateTime,
    val userId: UUID,
    val query: String,
) {
    companion object {
        fun from(submission: Submission): SubmissionDetailResponse {
            return SubmissionDetailResponse(
                id = submission.id!!,
                problemId = submission.problemId,
                status = submission.status,
                verdict = submission.verdict,
                createdAt = submission.createdAt,
                userId = submission.userId,
                query = submission.query,
            )
        }
    }
}
