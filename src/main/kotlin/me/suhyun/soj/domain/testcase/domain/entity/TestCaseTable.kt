package me.suhyun.soj.domain.testcase.domain.entity

import me.suhyun.soj.global.common.entity.BaseTable

object TestCaseTable : BaseTable("test_cases") {
    val problemId = long("problem_id")
    val initSql = text("init_sql").nullable()
    val answer = text("answer")
}
