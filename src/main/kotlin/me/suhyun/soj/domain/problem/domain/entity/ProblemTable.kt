package me.suhyun.soj.domain.problem.domain.entity

import me.suhyun.soj.domain.problem.domain.model.SchemaMetadata
import me.suhyun.soj.global.common.entity.BaseTable
import me.suhyun.soj.global.common.entity.json

@Suppress("MagicNumber")
object ProblemTable : BaseTable("problems") {
    val title = varchar("name", 128)
    val description = text("description")
    val schemaSql = text("schema_sql")
    val schemaMetadata = json<SchemaMetadata>("schema_metadata").nullable()
    val difficulty = integer(name = "difficulty")
    val timeLimit = integer("time_limit")
    val isOrderSensitive = bool("is_order_sensitive").default(false)
    val solvedCount = integer("solved_count").default(0)
    val submissionCount = integer("submitted_count").default(0)
}
