package me.suhyun.soj.domain.admin.presentation

import me.suhyun.soj.domain.admin.application.service.DashboardService
import me.suhyun.soj.domain.admin.presentation.response.AdminSubmissionsResponse
import me.suhyun.soj.domain.admin.presentation.response.DashboardStatisticsResponse
import me.suhyun.soj.domain.admin.presentation.response.RecentSubmissionResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class DashboardController(
    private val dashboardService: DashboardService
) {

    @GetMapping("/dashboard/statistics")
    fun getStatistics(): DashboardStatisticsResponse {
        return dashboardService.getStatistics()
    }

    @GetMapping("/submissions/recent")
    fun getRecentSubmissions(
        @RequestParam(defaultValue = "10") limit: Int
    ): List<RecentSubmissionResponse> {
        return dashboardService.getRecentSubmissions(limit)
    }

    @GetMapping("/submissions")
    fun getSubmissions(
        @RequestParam(required = false) problemId: Long?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): AdminSubmissionsResponse {
        val submissions = dashboardService.getSubmissions(problemId, page, size)
        val totalCount = dashboardService.getSubmissionsCount(problemId)
        return AdminSubmissionsResponse(
            submissions = submissions,
            totalCount = totalCount,
            page = page,
            size = size
        )
    }
}
