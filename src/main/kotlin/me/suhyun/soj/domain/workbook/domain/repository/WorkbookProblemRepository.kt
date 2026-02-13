package me.suhyun.soj.domain.workbook.domain.repository

import me.suhyun.soj.domain.workbook.domain.model.WorkbookProblem

interface WorkbookProblemRepository {
    fun save(workbookProblem: WorkbookProblem): WorkbookProblem
    fun findAllByWorkbookId(workbookId: Long): List<WorkbookProblem>
    fun findAllByWorkbookId(workbookId: Long, page: Int, size: Int): List<WorkbookProblem>
    fun countByWorkbookId(workbookId: Long): Long
    fun findByWorkbookIdAndProblemId(workbookId: Long, problemId: Long): WorkbookProblem?
    fun delete(workbookProblemId: Long): Boolean
}
