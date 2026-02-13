package me.suhyun.soj.domain.workbook.domain.entity

import me.suhyun.soj.global.common.entity.BaseTable

@Suppress("MagicNumber")
object WorkbookProblemTable : BaseTable("workbook_problems") {
    val workbookId = long("workbook_id")
    val problemId = long("problem_id")
}
