package me.suhyun.soj.domain.problem.domain.repository

import me.suhyun.soj.domain.problem.domain.entity.ProblemEntity
import me.suhyun.soj.domain.problem.domain.entity.ProblemTable
import me.suhyun.soj.domain.problem.domain.model.Problem
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ProblemRepositoryImpl : ProblemRepository {

    override fun save(problem: Problem): Problem {
        val entity = ProblemEntity.new {
            this.title = problem.title
            this.description = problem.description
            this.schemaSql = problem.schemaSql
            this.difficulty = problem.difficulty
            this.timeLimit = problem.timeLimit
            this.isOrderSensitive = problem.isOrderSensitive
            this.createdAt = LocalDateTime.now()
        }
        return Problem.from(entity)
    }

    override fun findById(id: Long): Problem? {
        return ProblemEntity.findById(id)
            ?.takeIf { it.deletedAt == null }
            ?.let { Problem.from(it) }
    }

    override fun findAll(
        page: Int,
        size: Int,
        difficulty: Int?,
        keyword: String?,
        sortBy: String,
        sortDirection: String
    ): List<Problem> {
        val sortOrder = if (sortDirection.uppercase() == "DESC") SortOrder.DESC else SortOrder.ASC
        val sortColumn = when (sortBy) {
            "title" -> ProblemTable.title
            "difficulty" -> ProblemTable.difficulty
            "solvedCount" -> ProblemTable.solvedCount
            else -> ProblemTable.id
        }

        val query = buildFilteredQuery(difficulty, keyword)

        return query
            .orderBy(sortColumn, sortOrder)
            .limit(size, (page * size).toLong())
            .map { ProblemEntity.wrapRow(it) }
            .map { Problem.from(it) }
    }

    override fun countAll(difficulty: Int?, keyword: String?): Long {
        val query = buildFilteredQuery(difficulty, keyword)

        return query.count()
    }

    override fun update(
        id: Long,
        title: String?,
        description: String?,
        schemaSql: String?,
        difficulty: Int?,
        timeLimit: Int?,
        isOrderSensitive: Boolean?
    ): Problem? {
        val entity = ProblemEntity.findById(id)
            ?.takeIf { it.deletedAt == null }
            ?: return null

        title?.let { entity.title = it }
        description?.let { entity.description = it }
        schemaSql?.let { entity.schemaSql = it }
        difficulty?.let { entity.difficulty = it }
        timeLimit?.let { entity.timeLimit = it }
        isOrderSensitive?.let { entity.isOrderSensitive = it }
        entity.updatedAt = LocalDateTime.now()

        return Problem.from(entity)
    }

    override fun softDelete(id: Long): Boolean {
        val entity = ProblemEntity.findById(id)
            ?.takeIf { it.deletedAt == null }
            ?: return false

        entity.deletedAt = LocalDateTime.now()
        return true
    }

    private fun buildFilteredQuery(difficulty: Int?, keyword: String?): Query {
        val query = ProblemTable.selectAll()
            .andWhere { ProblemTable.deletedAt.isNull() }

        difficulty?.let {
            query.andWhere { ProblemTable.difficulty eq it }
        }

        keyword?.let { kw ->
            val escaped = kw.replace("%", "\\%").replace("_", "\\_")
            val keywordAsId = kw.toLongOrNull()
            query.andWhere {
                if (keywordAsId != null) {
                    (ProblemTable.id eq keywordAsId) or (ProblemTable.title like "%$escaped%")
                } else {
                    ProblemTable.title like "%$escaped%"
                }
            }
        }
        return query
    }
}
