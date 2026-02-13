package me.suhyun.soj.domain.workbook.domain.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class WorkbookProblemEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<WorkbookProblemEntity>(WorkbookProblemTable)

    var workbookId by WorkbookProblemTable.workbookId
    var problemId by WorkbookProblemTable.problemId
    var createdAt by WorkbookProblemTable.createdAt
    var deletedAt by WorkbookProblemTable.deletedAt
}
