package me.suhyun.soj.domain.problem.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

@Suppress("MagicNumber")
object ProblemTable : LongIdTable("problems") {
    val title = varchar("name", 128)
    val description = text("description")
    val schemaSql = text("schema_sql")
    val difficulty = integer(name = "difficulty")
    val solvedCount = integer("solved_count").default(0)
    val submissionCount = integer("submitted_count").default(0)
    val created_at = datetime("created_at")
    val updated_at = datetime("updated_at").nullable()
    val deleted_at = datetime("deleted_at").nullable()
}
