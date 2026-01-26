package me.suhyun.soj.domain.testcase.domain.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TestCaseEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TestCaseEntity>(TestCaseTable)

    var problemId by TestCaseTable.problemId
    var initSql by TestCaseTable.initSql
    var answer by TestCaseTable.answer
    var createdAt by TestCaseTable.createdAt
    var updatedAt by TestCaseTable.updatedAt
    var deletedAt by TestCaseTable.deletedAt
}
