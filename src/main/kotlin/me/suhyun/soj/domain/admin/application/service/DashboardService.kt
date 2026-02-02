package me.suhyun.soj.domain.admin.application.service

import me.suhyun.soj.domain.admin.presentation.response.DailySubmission
import me.suhyun.soj.domain.admin.presentation.response.DashboardStatisticsResponse
import me.suhyun.soj.domain.admin.presentation.response.RecentSubmissionResponse
import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import me.suhyun.soj.domain.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

@Service
@Transactional(readOnly = true)
class DashboardService(
    private val problemRepository: ProblemRepository,
    private val submissionRepository: SubmissionRepository,
    private val userRepository: UserRepository
) {

    fun getStatistics(): DashboardStatisticsResponse {
        val totalProblems = problemRepository.countAll(null, null, null)
        val totalSubmissions = submissionRepository.countAll()
        val totalUsers = userRepository.countAll()
        val pendingCount = submissionRepository.countByStatus(SubmissionStatus.PENDING)

        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(6)
        val dailySubmissions = submissionRepository.countByDateGrouped(
            startDate.atStartOfDay(),
            endDate.atTime(LocalTime.MAX)
        ).map { DailySubmission(it.date, it.count) }

        return DashboardStatisticsResponse(
            totalProblems = totalProblems,
            totalSubmissions = totalSubmissions,
            totalUsers = totalUsers,
            pendingCount = pendingCount,
            dailySubmissions = dailySubmissions
        )
    }

    fun getRecentSubmissions(limit: Int): List<RecentSubmissionResponse> {
        return submissionRepository.findRecent(limit)
            .map { RecentSubmissionResponse.from(it) }
    }

    fun getSubmissions(problemId: Long?, page: Int, size: Int): List<RecentSubmissionResponse> {
        return submissionRepository.findAll(problemId, page, size)
            .map { RecentSubmissionResponse.from(it) }
    }

    fun getSubmissionsCount(problemId: Long?): Long {
        return submissionRepository.countAll(problemId)
    }
}
