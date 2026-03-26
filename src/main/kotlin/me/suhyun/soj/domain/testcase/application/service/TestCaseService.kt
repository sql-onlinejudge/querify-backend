package me.suhyun.soj.domain.testcase.application.service

import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.problem.exception.ProblemErrorCode
import me.suhyun.soj.domain.testcase.domain.model.TestCase
import me.suhyun.soj.domain.testcase.domain.repository.TestCaseRepository
import me.suhyun.soj.domain.testcase.exception.TestCaseErrorCode
import me.suhyun.soj.domain.testcase.infrastructure.mongo.TestCaseMetadataDocument
import me.suhyun.soj.domain.testcase.infrastructure.mongo.TestCaseMetadataMongoRepository
import me.suhyun.soj.domain.testcase.presentation.request.CreateTestCaseRequest
import me.suhyun.soj.domain.testcase.presentation.request.UpdateTestCaseRequest
import me.suhyun.soj.domain.testcase.presentation.response.TestCaseResponse
import me.suhyun.soj.global.common.util.SqlGenerator
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class TestCaseService(
    private val testCaseRepository: TestCaseRepository,
    private val problemRepository: ProblemRepository,
    private val testCaseMetadataMongoRepository: TestCaseMetadataMongoRepository
) {

    // TODO 보일 예시와 테스트 케이스 구분
    @CacheEvict(value = ["testcases"], allEntries = true)
    fun create(problemId: Long, request: CreateTestCaseRequest) {
        val problem = problemRepository.findById(problemId)
            ?: throw BusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND)

        val initSql = request.initData?.let { SqlGenerator.generateInit(it) }
        val answer = SqlGenerator.generateAnswer(request.answerData)
        val saved = testCaseRepository.save(
            TestCase(
                id = null,
                problemId = problem.id!!,
                initSql = initSql,
                initMetadata = null,
                answer = answer,
                answerMetadata = null,
                isVisible = request.isVisible,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null
            )
        )
        testCaseMetadataMongoRepository.save(
            TestCaseMetadataDocument(
                testCaseId = saved.id!!,
                initMetadata = request.initData,
                answerMetadata = request.answerData
            )
        )
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["testcases"], key = "#problemId + '_' + #isVisible")
    fun findAll(problemId: Long, isVisible: Boolean? = true): List<TestCaseResponse> {
        val testCases = testCaseRepository.findAllByProblemId(problemId, isVisible)
        val ids = testCases.mapNotNull { it.id }
        val metadataMap = testCaseMetadataMongoRepository.findByTestCaseIdIn(ids)
            .associateBy { it.testCaseId }
        return testCases.map { tc ->
            val meta = metadataMap[tc.id]
            TestCaseResponse.from(tc.copy(initMetadata = meta?.initMetadata, answerMetadata = meta?.answerMetadata))
        }
    }

    @Transactional(readOnly = true)
    fun findById(problemId: Long, testcaseId: Long): TestCaseResponse {
        val testCase = testCaseRepository.findById(testcaseId)
            ?: throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        if (testCase.problemId != problemId) {
            throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        }
        val meta = testCaseMetadataMongoRepository.findByTestCaseId(testcaseId)
        return TestCaseResponse.from(testCase.copy(initMetadata = meta?.initMetadata, answerMetadata = meta?.answerMetadata))
    }

    @CacheEvict(value = ["testcases"], allEntries = true)
    fun update(problemId: Long, testcaseId: Long, request: UpdateTestCaseRequest) {
        val testCase = testCaseRepository.findById(testcaseId)
            ?: throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        if (testCase.problemId != problemId) {
            throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        }
        val initSql = request.initData?.let { SqlGenerator.generateInit(it) }
        val answer = request.answerData?.let { SqlGenerator.generateAnswer(it) }
        testCaseRepository.update(testcaseId, initSql, answer, request.isVisible)

        if (request.initData != null || request.answerData != null) {
            val existing = testCaseMetadataMongoRepository.findByTestCaseId(testcaseId)
            val updated = existing?.copy(
                initMetadata = request.initData ?: existing.initMetadata,
                answerMetadata = request.answerData ?: existing.answerMetadata
            ) ?: TestCaseMetadataDocument(
                testCaseId = testcaseId,
                initMetadata = request.initData,
                answerMetadata = request.answerData
            )
            testCaseMetadataMongoRepository.save(updated)
        }
    }

    @CacheEvict(value = ["testcases"], allEntries = true)
    fun delete(problemId: Long, testcaseId: Long) {
        val testCase = testCaseRepository.findById(testcaseId)
            ?: throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        if (testCase.problemId != problemId) {
            throw BusinessException(TestCaseErrorCode.TEST_CASE_NOT_FOUND)
        }
        testCaseRepository.softDelete(testcaseId)
        testCaseMetadataMongoRepository.deleteByTestCaseId(testcaseId)
    }
}
