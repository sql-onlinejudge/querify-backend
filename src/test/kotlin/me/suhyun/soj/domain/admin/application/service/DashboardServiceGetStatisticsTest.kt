package me.suhyun.soj.domain.admin.application.service

import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.submission.domain.model.DailySubmissionCount
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import me.suhyun.soj.domain.user.domain.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class DashboardServiceGetStatisticsTest {

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

    @Test
    fun `should return statistics with correct counts`() {
        whenever(problemRepository.countAll(null, null, null)).thenReturn(10L)
        whenever(submissionRepository.countAll()).thenReturn(100L)
        whenever(userRepository.countAll()).thenReturn(50L)
        whenever(submissionRepository.countByStatus(SubmissionStatus.PENDING)).thenReturn(3L)
        whenever(submissionRepository.countByDateGrouped(any(), any())).thenReturn(emptyList())

        val result = dashboardService.getStatistics()

        assertThat(result.totalProblems).isEqualTo(10L)
        assertThat(result.totalSubmissions).isEqualTo(100L)
        assertThat(result.totalUsers).isEqualTo(50L)
        assertThat(result.pendingCount).isEqualTo(3L)
    }

    @Test
    fun `should include daily submissions for last 7 days`() {
        val today = LocalDate.now()
        val dailyCounts = listOf(
            DailySubmissionCount(today.minusDays(1), 5L),
            DailySubmissionCount(today, 10L)
        )

        whenever(problemRepository.countAll(null, null, null)).thenReturn(0L)
        whenever(submissionRepository.countAll()).thenReturn(0L)
        whenever(userRepository.countAll()).thenReturn(0L)
        whenever(submissionRepository.countByStatus(SubmissionStatus.PENDING)).thenReturn(0L)
        whenever(submissionRepository.countByDateGrouped(any(), any())).thenReturn(dailyCounts)

        val result = dashboardService.getStatistics()

        assertThat(result.dailySubmissions).hasSize(2)
        assertThat(result.dailySubmissions[0].date).isEqualTo(today.minusDays(1))
        assertThat(result.dailySubmissions[0].count).isEqualTo(5L)
        assertThat(result.dailySubmissions[1].date).isEqualTo(today)
        assertThat(result.dailySubmissions[1].count).isEqualTo(10L)
    }

    @Test
    fun `should return empty daily submissions when no data`() {
        whenever(problemRepository.countAll(null, null, null)).thenReturn(0L)
        whenever(submissionRepository.countAll()).thenReturn(0L)
        whenever(userRepository.countAll()).thenReturn(0L)
        whenever(submissionRepository.countByStatus(SubmissionStatus.PENDING)).thenReturn(0L)
        whenever(submissionRepository.countByDateGrouped(any(), any())).thenReturn(emptyList())

        val result = dashboardService.getStatistics()

        assertThat(result.dailySubmissions).isEmpty()
    }
}
