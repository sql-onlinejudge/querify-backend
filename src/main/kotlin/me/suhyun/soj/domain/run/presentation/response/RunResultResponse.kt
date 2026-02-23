package me.suhyun.soj.domain.run.presentation.response

data class RunResultResponse(
    val testCaseId: Long,
    val columns: List<String>,
    val rows: List<List<String>>,
    val errorMessage: String?
)
