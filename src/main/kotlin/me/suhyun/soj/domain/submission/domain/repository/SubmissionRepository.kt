package me.suhyun.soj.domain.submission.domain.repository

import me.suhyun.soj.domain.submission.domain.model.Submission
import java.util.UUID

interface SubmissionRepository {
    fun save(submission: Submission): Submission
    fun findById(id: Long): Submission?
    fun findByProblemId(problemId: Long, page: Int, size: Int): List<Submission>
    fun findByProblemIdAndUserId(problemId: Long, userId: UUID, page: Int, size: Int): List<Submission>
    fun countByProblemId(problemId: Long): Long
    fun countByProblemIdAndUserId(problemId: Long, userId: UUID): Long
    fun getTrialStatuses(problemIds: List<Long>, userId: UUID): Map<Long, Boolean>
}
