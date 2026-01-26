package me.suhyun.soj.domain.submission.domain.repository

import me.suhyun.soj.domain.submission.domain.entity.SubmissionEntity
import me.suhyun.soj.domain.submission.domain.entity.SubmissionTable
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import me.suhyun.soj.domain.submission.domain.model.Submission
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class SubmissionRepositoryImpl : SubmissionRepository {

    override fun save(submission: Submission): Submission {
        val entity = SubmissionEntity.new {
            this.problemId = submission.problemId
            this.userId = submission.userId
            this.query = submission.query
            this.status = submission.status
            this.verdict = submission.verdict
            this.createdAt = LocalDateTime.now()
        }
        return Submission.from(entity)
    }

    override fun findById(id: Long): Submission? {
        return SubmissionEntity.findById(id)
            ?.takeIf { it.deletedAt == null }
            ?.let { Submission.from(it) }
    }

    override fun findByProblemId(problemId: Long, page: Int, size: Int): List<Submission> {
        return SubmissionTable.selectAll()
            .andWhere { SubmissionTable.deletedAt.isNull() }
            .andWhere { SubmissionTable.problemId eq problemId }
            .orderBy(SubmissionTable.createdAt, SortOrder.DESC)
            .limit(size, (page * size).toLong())
            .map { SubmissionEntity.wrapRow(it) }
            .map { Submission.from(it) }
    }

    override fun findByProblemIdAndUserId(
        problemId: Long,
        userId: UUID,
        page: Int,
        size: Int
    ): List<Submission> {
        return SubmissionTable.selectAll()
            .andWhere { SubmissionTable.deletedAt.isNull() }
            .andWhere { SubmissionTable.problemId eq problemId }
            .andWhere { SubmissionTable.userId eq userId }
            .orderBy(SubmissionTable.createdAt, SortOrder.DESC)
            .limit(size, (page * size).toLong())
            .map { SubmissionEntity.wrapRow(it) }
            .map { Submission.from(it) }
    }

    override fun countByProblemId(problemId: Long): Long {
        return SubmissionTable.selectAll()
            .andWhere { SubmissionTable.deletedAt.isNull() }
            .andWhere { SubmissionTable.problemId eq problemId }
            .count()
    }

    override fun countByProblemIdAndUserId(problemId: Long, userId: UUID): Long {
        return SubmissionTable.selectAll()
            .andWhere { SubmissionTable.deletedAt.isNull() }
            .andWhere { SubmissionTable.problemId eq problemId }
            .andWhere { SubmissionTable.userId eq userId }
            .count()
    }

    override fun getTrialStatuses(problemIds: List<Long>, userId: UUID): Map<Long, Boolean> {
        if (problemIds.isEmpty()) return emptyMap()

        val submissions = SubmissionTable.selectAll()
            .andWhere { SubmissionTable.deletedAt.isNull() }
            .andWhere { SubmissionTable.problemId inList problemIds }
            .andWhere { SubmissionTable.userId eq userId }
            .map { SubmissionEntity.wrapRow(it) }

        return submissions.groupBy { it.problemId }
            .mapValues { (_, subs) -> subs.any { it.verdict == SubmissionVerdict.ACCEPTED } }
    }
}
