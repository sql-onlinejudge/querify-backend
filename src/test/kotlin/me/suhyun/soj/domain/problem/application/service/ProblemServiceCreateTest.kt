package me.suhyun.soj.domain.problem.application.service

import me.suhyun.soj.domain.problem.domain.model.Problem
import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.problem.presentation.request.CreateProblemRequest
import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import me.suhyun.soj.domain.testcase.domain.model.TestCase
import me.suhyun.soj.domain.testcase.domain.repository.TestCaseRepository
import me.suhyun.soj.domain.testcase.presentation.request.CreateTestCaseRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ProblemServiceCreateTest {

    @Mock
    private lateinit var problemRepository: ProblemRepository

    @Mock
    private lateinit var testCaseRepository: TestCaseRepository

    @Mock
    private lateinit var submissionRepository: SubmissionRepository

    private lateinit var problemService: ProblemService

    @BeforeEach
    fun setUp() {
        problemService = ProblemService(problemRepository, testCaseRepository, submissionRepository)
    }

    @Test
    fun `should create problem with testcases successfully`() {
        val request = CreateProblemRequest(
            title = "Test Problem",
            description = "Test Description",
            schemaSql = "CREATE TABLE test (id INT)",
            difficulty = 3,
            timeLimit = 1000,
            isOrderSensitive = false,
            testcases = listOf(
                CreateTestCaseRequest(initSql = "INSERT INTO test VALUES (1)", answer = "1")
            )
        )

        val savedProblem = Problem(
            id = 1L,
            title = request.title,
            description = request.description,
            schemaSql = request.schemaSql,
            difficulty = request.difficulty,
            timeLimit = request.timeLimit,
            isOrderSensitive = request.isOrderSensitive,
            solvedCount = 0,
            submissionCount = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            deletedAt = null
        )

        whenever(problemRepository.save(any())).thenReturn(savedProblem)
        whenever(testCaseRepository.save(any())).thenReturn(
            TestCase(
                id = 1L,
                problemId = 1L,
                initSql = "INSERT INTO test VALUES (1)",
                answer = "1",
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null
            )
        )

        problemService.create(request)

        verify(problemRepository).save(any())
        verify(testCaseRepository).save(any())
    }

    @Test
    fun `should save all testcases when creating problem`() {
        val testcases = listOf(
            CreateTestCaseRequest(initSql = "INSERT INTO test VALUES (1)", answer = "1"),
            CreateTestCaseRequest(initSql = "INSERT INTO test VALUES (2)", answer = "2"),
            CreateTestCaseRequest(initSql = null, answer = "3")
        )

        val request = CreateProblemRequest(
            title = "Test Problem",
            description = "Test Description",
            schemaSql = "CREATE TABLE test (id INT)",
            difficulty = 2,
            timeLimit = 500,
            isOrderSensitive = true,
            testcases = testcases
        )

        val savedProblem = Problem(
            id = 1L,
            title = request.title,
            description = request.description,
            schemaSql = request.schemaSql,
            difficulty = request.difficulty,
            timeLimit = request.timeLimit,
            isOrderSensitive = request.isOrderSensitive,
            solvedCount = 0,
            submissionCount = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            deletedAt = null
        )

        whenever(problemRepository.save(any())).thenReturn(savedProblem)
        whenever(testCaseRepository.save(any())).thenReturn(
            TestCase(
                id = 1L,
                problemId = 1L,
                initSql = null,
                answer = "1",
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null
            )
        )

        problemService.create(request)

        verify(problemRepository).save(any())
        verify(testCaseRepository, times(3)).save(any())
    }
}
