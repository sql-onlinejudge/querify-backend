package me.suhyun.soj.domain.problem.domain.repository

import me.suhyun.soj.domain.problem.domain.model.Problem
import me.suhyun.soj.domain.problem.domain.model.SchemaMetadata

interface ProblemRepository {
    fun save(problem: Problem): Problem
    fun findById(id: Long): Problem?
    fun findAll(
        page: Int,
        size: Int,
        minDifficulty: Int?,
        maxDifficulty: Int?,
        keyword: String?,
        sort: List<String>
    ): List<Problem>
    fun countAll(minDifficulty: Int?, maxDifficulty: Int?, keyword: String?): Long
    fun update(
        id: Long,
        title: String?,
        description: String?,
        schemaSql: String?,
        schemaMetadata: SchemaMetadata?,
        difficulty: Int?,
        timeLimit: Int?,
        isOrderSensitive: Boolean?
    ): Problem?
    fun softDelete(id: Long): Boolean
    fun incrementSubmittedCount(id: Long)
    fun incrementSolvedCount(id: Long)
}
