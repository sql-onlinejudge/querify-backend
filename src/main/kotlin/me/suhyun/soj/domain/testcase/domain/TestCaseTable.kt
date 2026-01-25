package me.suhyun.soj.domain.testcase.domain

import me.suhyun.soj.domain.problem.domain.entity.ProblemTable
import me.suhyun.soj.global.entity.BaseTable

object TestCaseTable : BaseTable("test_cases") {
    val problemId = reference("problem_id", ProblemTable)
    val initSql = text("init_sql").nullable()
    val answer = text("answer")
}
