package me.suhyun.soj.domain.run.presentation.response

data class RunResponse(
    val runId: Long,
    val status: String,
    val results: List<RunResultResponse>?
)
