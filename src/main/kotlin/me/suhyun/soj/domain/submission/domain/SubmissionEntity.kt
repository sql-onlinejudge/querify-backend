package me.suhyun.soj.domain.submission.domain

import me.suhyun.soj.domain.problem.domain.ProblemEntity
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SubmissionEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SubmissionEntity>(SubmissionTable)

    var problem by ProblemEntity referencedOn SubmissionTable.problemId
    var userId by SubmissionTable.userId
    var query by SubmissionTable.query
    var status by SubmissionTable.status
    var verdict by SubmissionTable.verdict
    var createdAt by SubmissionTable.createdAt
    var updatedAt by SubmissionTable.updatedAt
    var deletedAt by SubmissionTable.deletedAt
}
