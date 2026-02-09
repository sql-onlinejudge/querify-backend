package me.suhyun.soj.domain.admin.application.service

import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.submission.domain.model.Submission
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import me.suhyun.soj.domain.user.domain.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class DashboardServiceGetSubmissionsTest {

    @Mock
    private lateinit var problemRepository: ProblemRepository

    @Mock
    private lateinit var submissionRepository: SubmissionRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var dashboardService: DashboardService

    @BeforeEach
    fun setUp() {
        dashboardService = DashboardService(problemRepository, submissionRepository, userRepository)
    }

    private fun createSubmission(id: Long): Submission = Submission(
        id = id,
        problemId = 1L,
        userId = UUID.randomUUID(),
        query = "SELECT 1",
        status = SubmissionStatus.COMPLETED,
        verdict = SubmissionVerdict.ACCEPTED,
        createdAt = LocalDateTime.now(),
        updatedAt = null,
        deletedAt = null
    )

    @Nested
    inner class GetSubmissions {

        @Test
        fun `should return submissions with pagination`() {
            val submissions = listOf(createSubmission(1L))
            whenever(submissionRepository.findAll(null, 0, 10)).thenReturn(submissions)

            val result = dashboardService.getSubmissions(null, 0, 10)

            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(1L)
        }

        @Test
        fun `should filter submissions by problemId`() {
            val submissions = listOf(createSubmission(1L))
            whenever(submissionRepository.findAll(5L, 0, 10)).thenReturn(submissions)

            val result = dashboardService.getSubmissions(5L, 0, 10)

            assertThat(result).hasSize(1)
        }
    }

    @Nested
    inner class GetSubmissionsCount {

        @Test
        fun `should count all submissions without filter`() {
            whenever(submissionRepository.countAll(null)).thenReturn(100L)

            val result = dashboardService.getSubmissionsCount(null)

            assertThat(result).isEqualTo(100L)
        }

        @Test
        fun `should count submissions by problemId`() {
            whenever(submissionRepository.countAll(5L)).thenReturn(20L)

            val result = dashboardService.getSubmissionsCount(5L)

            assertThat(result).isEqualTo(20L)
        }
    }
}
