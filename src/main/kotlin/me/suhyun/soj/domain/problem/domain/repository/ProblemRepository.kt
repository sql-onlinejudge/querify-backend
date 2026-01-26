package me.suhyun.soj.domain.problem.domain.repository

import me.suhyun.soj.domain.problem.domain.model.Problem

interface ProblemRepository {
    fun save(problem: Problem): Problem
    fun findById(id: Long): Problem?
    fun findAll(
        page: Int,
        size: Int,
        difficulty: Int?,
        keyword: String?,
        sortBy: String,
        sortDirection: String
    ): List<Problem>
    fun countAll(difficulty: Int?, keyword: String?): Long
    fun update(
        id: Long,
        title: String?,
        description: String?,
        schemaSql: String?,
        difficulty: Int?,
        timeLimit: Int?,
        isOrderSensitive: Boolean?
    ): Problem?
    fun softDelete(id: Long): Boolean
}
