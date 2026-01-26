package me.suhyun.soj.domain.submission.presentation.response

import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import me.suhyun.soj.domain.submission.domain.model.Submission
import java.time.LocalDateTime

data class SubmissionResponse(
    val id: Long,
    val problemId: Long,
    val status: SubmissionStatus,
    val verdict: SubmissionVerdict?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(submission: Submission): SubmissionResponse {
            return SubmissionResponse(
                id = submission.id!!,
                problemId = submission.problemId,
                status = submission.status,
                verdict = submission.verdict,
                createdAt = submission.createdAt
            )
        }
    }
}
