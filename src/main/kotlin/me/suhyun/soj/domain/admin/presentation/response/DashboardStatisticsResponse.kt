package me.suhyun.soj.domain.admin.presentation.response

import java.time.LocalDate

data class DashboardStatisticsResponse(
    val totalProblems: Long,
    val totalSubmissions: Long,
    val totalUsers: Long,
    val pendingCount: Long,
    val dailySubmissions: List<DailySubmission>
)

data class DailySubmission(
    val date: LocalDate,
    val count: Long
)
