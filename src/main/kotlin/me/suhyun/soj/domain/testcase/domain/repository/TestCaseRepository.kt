package me.suhyun.soj.domain.testcase.domain.repository

import me.suhyun.soj.domain.testcase.domain.model.TestCase

interface TestCaseRepository {
    fun save(testCase: TestCase): TestCase
    fun findById(id: Long): TestCase?
    fun findAllByProblemId(problemId: Long, isVisible: Boolean? = true): List<TestCase>
    fun update(id: Long, initSql: String?, answer: String?, isVisible: Boolean?): TestCase?
    fun softDelete(id: Long): Boolean
}
