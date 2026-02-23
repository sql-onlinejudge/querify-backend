package me.suhyun.soj.domain.run.domain.entity

import me.suhyun.soj.domain.run.domain.model.enums.RunStatus
import me.suhyun.soj.global.common.entity.BaseTable

object RunTable : BaseTable("runs") {
    val problemId = long("problem_id")
    val query = text("query")
    val status = enumerationByName<RunStatus>("status", 20)
    val result = text("result").nullable()
    val errorMessage = varchar("error_message", 500).nullable()
}
