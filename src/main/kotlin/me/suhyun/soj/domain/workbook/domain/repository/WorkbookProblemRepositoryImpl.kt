package me.suhyun.soj.domain.workbook.domain.repository

import me.suhyun.soj.domain.workbook.domain.entity.WorkbookProblemEntity
import me.suhyun.soj.domain.workbook.domain.entity.WorkbookProblemTable
import me.suhyun.soj.domain.workbook.domain.model.WorkbookProblem
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class WorkbookProblemRepositoryImpl : WorkbookProblemRepository {

    override fun save(workbookProblem: WorkbookProblem): WorkbookProblem {
        val entity = WorkbookProblemEntity.new {
            this.workbookId = workbookProblem.workbookId
            this.problemId = workbookProblem.problemId
        }
        return WorkbookProblem.from(entity)
    }

    override fun findAllByWorkbookId(workbookId: Long): List<WorkbookProblem> {
        return WorkbookProblemTable.selectAll()
            .andWhere { WorkbookProblemTable.workbookId eq workbookId }
            .map { WorkbookProblemEntity.wrapRow(it) }
            .map { WorkbookProblem.from(it) }
    }

    override fun findAllByWorkbookId(workbookId: Long, page: Int, size: Int): List<WorkbookProblem> {
        return WorkbookProblemTable.selectAll()
            .andWhere { WorkbookProblemTable.workbookId eq workbookId }
            .limit(size, (page * size).toLong())
            .map { WorkbookProblemEntity.wrapRow(it) }
            .map { WorkbookProblem.from(it) }
    }

    override fun countByWorkbookId(workbookId: Long): Long {
        return WorkbookProblemTable.selectAll()
            .andWhere { WorkbookProblemTable.workbookId eq workbookId }
            .count()
    }

    override fun findByWorkbookIdAndProblemId(workbookId: Long, problemId: Long): WorkbookProblem? {
        return WorkbookProblemTable.selectAll()
            .andWhere {
                (WorkbookProblemTable.workbookId eq workbookId) and
                    (WorkbookProblemTable.problemId eq problemId)
            }
            .map { WorkbookProblemEntity.wrapRow(it) }
            .map { WorkbookProblem.from(it) }
            .firstOrNull()
    }

    override fun delete(workbookProblemId: Long): Boolean {
        val entity = WorkbookProblemEntity.findById(workbookProblemId)
            ?: return false
        entity.delete()
        return true
    }
}
