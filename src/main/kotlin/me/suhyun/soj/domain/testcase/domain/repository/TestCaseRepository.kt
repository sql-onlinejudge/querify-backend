package me.suhyun.soj.domain.testcase.domain.repository

import me.suhyun.soj.domain.testcase.domain.model.AnswerMetadata
import me.suhyun.soj.domain.testcase.domain.model.InitMetadata
import me.suhyun.soj.domain.testcase.domain.model.TestCase

interface TestCaseRepository {
    fun save(testCase: TestCase): TestCase
    fun findById(id: Long): TestCase?
    fun findAllByProblemId(problemId: Long): List<TestCase>
    fun update(id: Long, initSql: String?, initMetadata: InitMetadata?, answer: String?, answerMetadata: AnswerMetadata?): TestCase?
    fun softDelete(id: Long): Boolean
}
