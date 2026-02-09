package me.suhyun.soj.domain.admin.application.service

import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.submission.domain.model.Submission
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import me.suhyun.soj.domain.user.domain.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class DashboardServiceGetRecentSubmissionsTest {

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

    @Test
    fun `should return recent submissions with limit`() {
        val submissions = listOf(createSubmission(1L), createSubmission(2L))
        whenever(submissionRepository.findRecent(5)).thenReturn(submissions)

        val result = dashboardService.getRecentSubmissions(5)

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[1].id).isEqualTo(2L)
    }

    @Test
    fun `should return empty list when no submissions`() {
        whenever(submissionRepository.findRecent(5)).thenReturn(emptyList())

        val result = dashboardService.getRecentSubmissions(5)

        assertThat(result).isEmpty()
    }
}
