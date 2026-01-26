package me.suhyun.soj.domain.testcase.application.service

import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.problem.exception.ProblemErrorCode
import me.suhyun.soj.domain.testcase.domain.model.TestCase
import me.suhyun.soj.domain.testcase.domain.repository.TestCaseRepository
import me.suhyun.soj.domain.testcase.exception.TestCaseErrorCode
import me.suhyun.soj.domain.testcase.presentation.request.CreateTestCaseRequest
import me.suhyun.soj.domain.testcase.presentation.request.UpdateTestCaseRequest
import me.suhyun.soj.domain.testcase.presentation.response.TestCaseResponse
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class TestCaseService(
    private val testCaseRepository: TestCaseRepository,
    private val problemRepository: ProblemRepository,
) {

    fun create(problemId: Long, request: CreateTestCaseRequest) {
        val problem = problemRepository.findById(problemId)
            ?: throw BusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND)

        testCaseRepository.save(
            TestCase(
                id = null,
                problemId = problem.id!!,
                initSql = request.initSql,
                answer = request.answer,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null
            )
        )
    }

    @Transactional(readOnly = true)
    fun findAll(problemId: Long): List<TestCaseResponse> {
        return testCaseRepository.findAllByProblemId(problemId)
            .map { TestCaseResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun findById(problemId: Long, testcaseId: Long): TestCaseResponse {
        val testCase = testCaseRepository.findById(testcaseId)
            ?: throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        if (testCase.problemId != problemId) {
            throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        }
        return TestCaseResponse.from(testCase)
    }

    fun update(problemId: Long, testcaseId: Long, request: UpdateTestCaseRequest) {
        val testCase = testCaseRepository.findById(testcaseId)
            ?: throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        if (testCase.problemId != problemId) {
            throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        }
        testCaseRepository.update(testcaseId, request.initSql, request.answer)
    }

    fun delete(problemId: Long, testcaseId: Long) {
        val testCase = testCaseRepository.findById(testcaseId)
            ?: throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        if (testCase.problemId != problemId) {
            throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        }
        testCaseRepository.softDelete(testcaseId)
    }
}
