package me.suhyun.soj.domain.submission.domain.repository

import me.suhyun.soj.domain.problem.domain.model.enums.TrialStatus
import me.suhyun.soj.domain.submission.domain.model.Submission
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import java.util.UUID

interface SubmissionRepository {
    fun save(submission: Submission): Submission
    fun findById(id: Long): Submission?
    fun findByProblemId(problemId: Long, page: Int, size: Int): List<Submission>
    fun findByProblemIdAndUserId(problemId: Long, userId: UUID, page: Int, size: Int): List<Submission>
    fun countByProblemId(problemId: Long): Long
    fun countByProblemIdAndUserId(problemId: Long, userId: UUID): Long
    fun getTrialStatuses(problemIds: List<Long>, userId: UUID): Map<Long, Boolean>
    fun getTrialStatus(problemId: Long, userId: UUID): TrialStatus
    fun updateStatus(id: Long, status: SubmissionStatus, verdict: SubmissionVerdict?): Boolean
}
