package me.suhyun.soj.domain.admin.presentation.response

import me.suhyun.soj.domain.submission.domain.model.Submission
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import java.time.LocalDateTime

data class RecentSubmissionResponse(
    val id: Long,
    val problemId: Long,
    val userId: String,
    val status: SubmissionStatus,
    val verdict: SubmissionVerdict?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(submission: Submission): RecentSubmissionResponse {
            return RecentSubmissionResponse(
                id = submission.id!!,
                problemId = submission.problemId,
                userId = submission.userId.toString(),
                status = submission.status,
                verdict = submission.verdict,
                createdAt = submission.createdAt
            )
        }
    }
}
