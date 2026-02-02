package me.suhyun.soj.domain.admin.presentation.response

data class AdminSubmissionsResponse(
    val submissions: List<RecentSubmissionResponse>,
    val totalCount: Long,
    val page: Int,
    val size: Int
)
