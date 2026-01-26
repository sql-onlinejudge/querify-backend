package me.suhyun.soj.domain.problem.application.service

import me.suhyun.soj.domain.problem.domain.model.Problem
import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.problem.exception.ProblemErrorCode
import me.suhyun.soj.domain.problem.presentation.request.UpdateProblemRequest
import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import me.suhyun.soj.domain.testcase.domain.repository.TestCaseRepository
import me.suhyun.soj.global.exception.BusinessException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ProblemServiceUpdateTest {

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
    fun `should update problem successfully`() {
        val request = UpdateProblemRequest(
            title = "Updated Title",
            description = "Updated Description",
            schemaSql = "CREATE TABLE updated (id INT)",
            difficulty = 5,
            timeLimit = 2000,
            isOrderSensitive = true
        )

        val updatedProblem = Problem(
            id = 1L,
            title = "Updated Title",
            description = "Updated Description",
            schemaSql = "CREATE TABLE updated (id INT)",
            difficulty = 5,
            timeLimit = 2000,
            isOrderSensitive = true,
            solvedCount = 0,
            submissionCount = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        whenever(
            problemRepository.update(
                id = 1L,
                title = request.title,
                description = request.description,
                schemaSql = request.schemaSql,
                difficulty = request.difficulty,
                timeLimit = request.timeLimit,
                isOrderSensitive = request.isOrderSensitive
            )
        ).thenReturn(updatedProblem)

        problemService.update(1L, request)

        verify(problemRepository).update(
            id = 1L,
            title = request.title,
            description = request.description,
            schemaSql = request.schemaSql,
            difficulty = request.difficulty,
            timeLimit = request.timeLimit,
            isOrderSensitive = request.isOrderSensitive
        )
    }

    @Test
    fun `should update only provided fields (partial update)`() {
        val request = UpdateProblemRequest(
            title = "Only Title Updated",
            description = null,
            schemaSql = null,
            difficulty = null,
            timeLimit = null,
            isOrderSensitive = null
        )

        val updatedProblem = Problem(
            id = 1L,
            title = "Only Title Updated",
            description = "Original Description",
            schemaSql = "CREATE TABLE test (id INT)",
            difficulty = 3,
            timeLimit = 1000,
            isOrderSensitive = false,
            solvedCount = 0,
            submissionCount = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        whenever(
            problemRepository.update(
                id = 1L,
                title = request.title,
                description = null,
                schemaSql = null,
                difficulty = null,
                timeLimit = null,
                isOrderSensitive = null
            )
        ).thenReturn(updatedProblem)

        problemService.update(1L, request)

        verify(problemRepository).update(
            id = 1L,
            title = "Only Title Updated",
            description = null,
            schemaSql = null,
            difficulty = null,
            timeLimit = null,
            isOrderSensitive = null
        )
    }

    @Test
    fun `should throw PROBLEM_NOT_FOUND when problem does not exist`() {
        val request = UpdateProblemRequest(title = "Updated Title")

        whenever(
            problemRepository.update(
                id = 999L,
                title = request.title,
                description = null,
                schemaSql = null,
                difficulty = null,
                timeLimit = null,
                isOrderSensitive = null
            )
        ).thenReturn(null)

        assertThatThrownBy { problemService.update(999L, request) }
            .isInstanceOf(BusinessException::class.java)
            .extracting("errorCode")
            .isEqualTo(ProblemErrorCode.PROBLEM_NOT_FOUND)
    }
}
