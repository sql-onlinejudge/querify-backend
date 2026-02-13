package me.suhyun.soj.domain.workbook.domain.model

import me.suhyun.soj.domain.workbook.domain.entity.WorkbookProblemEntity

data class WorkbookProblem(
    val id: Long?,
    val workbookId: Long,
    val problemId: Long,
) {
    companion object {
        fun from(entity: WorkbookProblemEntity) = WorkbookProblem(
            id = entity.id.value,
            workbookId = entity.workbookId,
            problemId = entity.problemId,
        )
    }
}
